package com.example.pointiki.data.source

import android.util.Log
import com.example.pointiki.models.Achievement
import com.example.pointiki.models.AchievementType
import com.example.pointiki.models.EventModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import java.util.concurrent.CancellationException
import javax.inject.Inject

class FirebaseAchievementsDataSource @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : AchievementsDataSource {

    override fun getAchievements(organizationId: String): Flow<List<Achievement>> {
        return callbackFlow {
            val achievementsCollectionRef = firebaseFirestore
                .collection(COLLECTION_DATA)
                .document(organizationId)
                .collection(COLLECTION_ACHIEVEMENTS)

            val listener = achievementsCollectionRef
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(CancellationException("Failed to get achievements: ${error.message}"))
                    } else if (snapshot != null && !snapshot.isEmpty) {
                        val achievements = snapshot.documents.mapNotNull { doc ->
                            val id = UUID.fromString(doc.id)
                            val achievementMap = doc.data
                            if (achievementMap != null) {
                                val imageUrl = achievementMap[FIELD_IMAGE_URL] as String
                                val name = achievementMap[FIELD_NAME] as String
                                val typeString = achievementMap[FIELD_TYPE] as String
                                val points = (achievementMap[FIELD_POINTS] as Long).toInt()
                                val commitment = (achievementMap[FIELD_COMMITMENT] as Long).toInt()
                                val order = (achievementMap[FIELD_ORDER] as Long).toInt()

                                val type = try {
                                    enumValueOf<AchievementType>(typeString)
                                } catch (e: IllegalArgumentException) {
                                    AchievementType.OTHER
                                }

                                Achievement(
                                    id = id,
                                    name = name,
                                    imageUrl = imageUrl,
                                    points = points,
                                    commitment = commitment,
                                    type = type,
                                    order = order
                                )
                            } else {
                                null
                            }
                        }
                        trySend(achievements)
                    } else {
                        Log.d("FirebaseAchievementsDataSource", "No achievements found")
                    }
                }

            awaitClose { listener.remove() }
        }
    }

    companion object {
        const val COLLECTION_DATA = "data"
        const val COLLECTION_ACHIEVEMENTS = "achievements"
        const val FIELD_TYPE = "type"
        const val FIELD_IMAGE_URL = "imageUrl"
        const val FIELD_NAME = "name"
        const val FIELD_POINTS = "points"
        const val FIELD_ORDER = "order"
        const val FIELD_COMMITMENT = "commitment"
    }
}