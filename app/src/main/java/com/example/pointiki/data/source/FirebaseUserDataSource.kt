package com.example.pointiki.data.source

import android.net.Uri
import android.util.Log
import com.example.pointiki.models.Achievement
import com.example.pointiki.models.AchievementType
import com.example.pointiki.models.AchievementUser
import com.example.pointiki.models.CurrentProgressUser
import com.example.pointiki.models.PointEntry
import com.example.pointiki.models.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.pointiki.utils.Result
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.Date
import java.util.UUID
import java.util.concurrent.CancellationException
import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : UserDataSource {

    override suspend fun createUser(organizationId: String, user: UserModel): Result<Unit> {
        return try {
            firebaseFirestore
                .collection(COLLECTION_DATA)
                .document(organizationId)
                .collection(COLLECTION_UNCONFIRMED_USERS)
                .document(user.id.toString())
                .set(user)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override fun getUser(organizationId: String, userId: UUID): Flow<UserModel> {
        return callbackFlow {
            val userDocumentRef = firebaseFirestore
                .collection(COLLECTION_DATA)
                .document(organizationId)
                .collection(COLLECTION_USERS)
                .document(userId.toString())

            val listenerRegistration = userDocumentRef
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(CancellationException("Failed to get user data: ${error.message.toString()}"))
                    } else if (snapshot != null && snapshot.exists()) {
                        val userMap = snapshot.data
                        if (userMap != null) {
                            val user = UserModel(
                                id = userId,
                                imageUrl = userMap[FIELD_IMAGE_URL] as String,
                                name = userMap[FIELD_NAME] as String,
                                points = (userMap[FIELD_POINTS] as Long).toInt(),
                                phone = userMap[FIELD_PHONE] as String,
                                pointsHistory = (userMap[FIELD_POINTS_HISTORY] as List<Map<String, Any>>)
                                    .map {
                                        PointEntry(
                                            points = (it[FIELD_POINTS] as Long).toInt(),
                                            date = (it[FIELD_DATE] as Timestamp).toDate()
                                        )
                                    },
                                achievements = (userMap[FIELD_ACHIEVEMENTS] as List<Map<String, Any>>)
                                    .map {
                                        AchievementUser(
                                            id = UUID.fromString(it[FIELD_ID] as String),
                                            date = (it[FIELD_DATE] as Timestamp).toDate()
                                        )
                                    },
                                progresses = (userMap[FIELD_PROGRESSES] as List<Map<String, Any>>).map {
                                    val strType = it[FIELD_TYPE] as String
                                    CurrentProgressUser(
                                        id = UUID.fromString(it[FIELD_ID] as String),
                                        progress = (it[FIELD_PROGRESS] as Long).toInt(),
                                        type = stringToAchievementType(strType)
                                    )
                                }
                            )
                            trySend(user)
                        } else {
                            Log.d("FirebaseUserDataSource", "User data is null")
                        }
                    } else {
                        Log.d("FirebaseUserDataSource", "Document is null")
                    }
                }
            awaitClose { listenerRegistration.remove() }
        }
    }

    override fun getAllUsers(organizationId: String): Flow<List<UserModel>> {
        return callbackFlow {
            val usersCollectionRef = firebaseFirestore
                .collection(COLLECTION_DATA)
                .document(organizationId)
                .collection(COLLECTION_USERS)

            val listenerRegistration = usersCollectionRef
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(CancellationException("Failed to get users: ${error.message}"))
                    } else if (snapshot != null && !snapshot.isEmpty) {
                        val users = snapshot.documents.mapNotNull { document ->
                            document.data?.let { userMap ->
                                val userId = UUID.fromString(document.id)
                                val imageUrl = userMap[FIELD_IMAGE_URL] as String
                                val name = userMap[FIELD_NAME] as String
                                val points = (userMap[FIELD_POINTS] as Long).toInt()
                                val phone = userMap[FIELD_PHONE] as String
                                val pointsHistory =
                                    (userMap[FIELD_POINTS_HISTORY] as List<Map<String, Any>>).map {
                                        val points = (it[FIELD_POINTS] as Long).toInt()
                                        val date = (it[FIELD_DATE] as Timestamp).toDate()
                                        PointEntry(points, date)
                                    }
                                val achievements =
                                    (userMap[FIELD_ACHIEVEMENTS] as List<Map<String, Any>>).map {
                                        AchievementUser(
                                            id = UUID.fromString(it[FIELD_ID] as String),
                                            date = (it[FIELD_DATE] as Timestamp).toDate()
                                        )
                                    }
                                val progresses = (userMap[FIELD_PROGRESSES] as List<Map<String, Any>>).map {
                                    val strType = it[FIELD_TYPE] as String
                                    CurrentProgressUser(
                                        id = UUID.fromString(it[FIELD_ID] as String),
                                        progress = (it[FIELD_PROGRESS] as Long).toInt(),
                                        type = stringToAchievementType(strType)
                                    )
                                }
                                UserModel(
                                    id = userId,
                                    imageUrl = imageUrl,
                                    name = name,
                                    points = points,
                                    phone = phone,
                                    pointsHistory = pointsHistory,
                                    achievements = achievements,
                                    progresses = progresses
                                )
                            }
                        }
                        trySend(users)
                    } else {
                        Log.d("FirebaseUserDataSource", "No users found")
                    }
                }

            awaitClose { listenerRegistration.remove() }
        }
    }

    override suspend fun setNextAchievementProgress(organizationId: String, userId: UUID, newId: UUID, achievement: Achievement): Result<Unit> {
        return try {
            firebaseFirestore.runTransaction { transaction ->
                val userDocumentRef = firebaseFirestore
                    .collection(COLLECTION_DATA)
                    .document(organizationId)
                    .collection(COLLECTION_USERS)
                    .document(userId.toString())
                val snapshot = transaction.get(userDocumentRef)
                val userMap = snapshot.data.orEmpty()

                // Update progresses
                val progresses = userMap[FIELD_PROGRESSES] as? List<Map<String, Any>> ?: emptyList()
                val updatedProgresses = progresses.map {
                    val currentId = UUID.fromString(it[FIELD_ID] as String)
                    if (currentId == achievement.id) {
                        mapOf(
                            FIELD_ID to newId.toString(),
                            FIELD_PROGRESS to 0L,
                            FIELD_TYPE to achievement.type.toString()
                        )
                    } else {
                        it
                    }
                }

                // Update achievements
                val achievements = userMap[FIELD_ACHIEVEMENTS] as? List<Map<String, Any>> ?: emptyList()
                val updatedAchievements = achievements + mapOf(
                    FIELD_ID to achievement.id.toString(),
                    FIELD_DATE to Timestamp(Date())
                )

                // Update points
                val currentPoints = userMap[FIELD_POINTS] as? Long ?: 0L
                val updatedPoints = currentPoints + achievement.points

                // Update points history
                val pointsHistory = userMap[FIELD_POINTS_HISTORY] as? MutableList<Map<String, Any>>
                    ?: mutableListOf()
                val pointEntryMap = mapOf(
                    FirebaseVisitsDataSource.FIELD_POINTS to achievement.points,
                    FirebaseVisitsDataSource.FIELD_DATE to Date()
                )
                pointsHistory.add(pointEntryMap)

                transaction.update(
                    userDocumentRef,
                    mapOf(
                        FIELD_PROGRESSES to updatedProgresses,
                        FIELD_ACHIEVEMENTS to updatedAchievements,
                        FIELD_POINTS to updatedPoints,
                        FIELD_POINTS_HISTORY to pointsHistory
                    )
                )
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    private fun stringToAchievementType(value: String): AchievementType {
        val type = try {
            enumValueOf<AchievementType>(value)
        } catch (e: IllegalArgumentException) {
            AchievementType.OTHER
        }
        return type
    }

    companion object {
        const val COLLECTION_DATA = "data"
        const val COLLECTION_USERS = "users"
        const val COLLECTION_UNCONFIRMED_USERS = "unconfirmedUsers"
        const val FIELD_ID = "id"
        const val FIELD_IMAGE_URL = "imageUrl"
        const val FIELD_NAME = "name"
        const val FIELD_PHONE = "phone"
        const val FIELD_POINTS = "points"
        const val FIELD_POINTS_HISTORY = "pointsHistory"
        const val FIELD_DATE = "date"
        const val FIELD_TYPE = "type"
        const val FIELD_PROGRESSES = "currentProgress"
        const val FIELD_PROGRESS = "progress"
        const val FIELD_ACHIEVEMENTS = "achievements"
    }

}