package com.example.pointiki.data.source

import android.util.Log
import com.example.pointiki.models.AchievementType
import com.example.pointiki.models.AchievementUser
import com.example.pointiki.models.CurrentProgressUser
import com.example.pointiki.models.EventParticipantModel
import com.example.pointiki.models.PointEntry
import com.example.pointiki.models.UserModel
import com.example.pointiki.models.VisitEventModel
import com.example.pointiki.utils.Result
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class FirebaseVisitsDataSource @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : VisitsDataSource {

    override suspend fun getVisitEventModel(organizationId: String, eventId: UUID): VisitEventModel? {
        val visitsCollectionRef = firebaseFirestore
            .collection(COLLECTION_DATA)
            .document(organizationId)
            .collection(COLLECTION_VISITS)
            .document(eventId.toString())

        /*val visitsSnapshot = visitsCollectionRef
            .whereEqualTo(FIELD_EVENT_ID, eventId.toString())
            .get()
            .await()*/

        //for (document in visitsSnapshot.documents) {
        val snapshot = visitsCollectionRef.get().await()
            val visitModel = snapshot.data
            if (visitModel != null) {
                return VisitEventModel(
                    id = UUID.fromString(visitModel[FIELD_ID] as String),
                    eventId = eventId,
                    participants = (visitModel[FIELD_PARTICIPANTS] as List<Map<String, Any>>).map {
                        EventParticipantModel(
                            id = UUID.fromString(it[FIELD_ID] as String),
                            date = (it[FIELD_DATE] as Timestamp).toDate()
                        )
                    }
                )
            } else {
                Log.d("FirebaseVisitsDataSource", "Visits data is null")
            }
       // }

        Log.d("FirebaseVisitsDataSource", "Document is null")
        return null
    }

    override suspend fun setVisit(
        organizationId: String,
        userId: UUID,
        eventId: UUID
    ): Result<Unit> {
        return try {
            val eventDocRef = firebaseFirestore
                .collection(COLLECTION_DATA)
                .document(organizationId)
                .collection(COLLECTION_VISITS)
                .document(eventId.toString())

            firebaseFirestore.runTransaction { transaction ->
                val snapshot = transaction.get(eventDocRef)
                val eventMap = snapshot.data

                if (eventMap != null) {
                    // Extract participants list from existing event
                    val participants = (eventMap[FIELD_PARTICIPANTS] as? List<Map<String, Any>>)?.toMutableList() ?: mutableListOf()

                    // Add new participant
                    val newParticipant = mapOf(
                        FIELD_ID to userId.toString(),
                        FIELD_DATE to Timestamp(Date())
                    )
                    participants.add(newParticipant)

                    // Update the existing event with new participants list
                    eventMap[FIELD_PARTICIPANTS] = participants

                    // Update the event document in Firestore
                    transaction.set(eventDocRef, eventMap)
                } else {
                    // If the event doesn't exist yet, create it
                    val newVisit = mapOf(
                        FIELD_ID to UUID.randomUUID().toString(),
                        FIELD_EVENT_ID to eventId.toString(),
                        FIELD_PARTICIPANTS to listOf(
                            mapOf(
                                FIELD_ID to userId.toString(),
                                FIELD_DATE to Timestamp(Date())
                            )
                        )
                    )
                    // Create the event document in Firestore
                    transaction.set(eventDocRef, newVisit)
                }
            }.await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun setPointsAndVisit(
        organizationId: String,
        userId: UUID,
        eventId: UUID,
        points: Int
    ): Result<Unit> {
        return try {
            val userRef = firebaseFirestore
                .collection(COLLECTION_DATA)
                .document(organizationId)
                .collection(COLLECTION_USERS)
                .document(userId.toString())

            firebaseFirestore.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val userData = snapshot.data
                if (userData != null) {
                    // Update points
                    val currentPoints = userData[FIELD_POINTS] as? Long ?: 0L
                    val updatedPoints = currentPoints + points
                    userData[FIELD_POINTS] = updatedPoints

                    // Update points history
                    val pointsHistory = userData[FirebaseUserDataSource.FIELD_POINTS_HISTORY] as? MutableList<Map<String, Any>>
                        ?: mutableListOf()
                    val pointEntryMap = mapOf(
                        FIELD_POINTS to points,
                        FIELD_DATE to Date()
                    )
                    pointsHistory.add(pointEntryMap)
                    userData[FIELD_POINTS_HISTORY] = pointsHistory

                    //Update current progress
                    val currentProgresses = userData[FIELD_PROGRESSES] as? MutableList<Map<String, Any>>
                        ?: mutableListOf()
                    val updatedProgresses = currentProgresses.toMutableList()
                    val visits = AchievementType.VISITS.toString()
                    val currentProgress = currentProgresses.find { it[FIELD_TYPE] == visits  }
                    val index = updatedProgresses.indexOfFirst {  it[FIELD_TYPE] == visits }
                    currentProgress?.let {
                        val id = it[FIELD_ID] as? String ?: ""
                        var progress = it[FIELD_PROGRESS] as? Long ?: 0L
                        progress += 1
                        val progressEntryMap = mapOf(
                            FIELD_ID to id,
                            FIELD_PROGRESS to progress,
                            FIELD_TYPE to visits
                        )
                        updatedProgresses[index] = progressEntryMap
                    }
                    userData[FIELD_PROGRESSES] = updatedProgresses

                    transaction.set(userRef, userData)
                } else {
                    return@runTransaction Result.Failure(Throwable("User not found"))
                }
            }.await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    companion object {
        const val COLLECTION_DATA = "data"
        const val COLLECTION_VISITS = "visits"
        const val COLLECTION_USERS = "users"
        const val FIELD_ID = "id"
        const val FIELD_PROGRESSES = "currentProgress"
        const val FIELD_PROGRESS = "progress"
        const val FIELD_POINTS_HISTORY = "pointsHistory"
        const val FIELD_POINTS = "points"
        const val FIELD_TYPE = "type"
        const val FIELD_EVENT_ID = "eventId"
        const val FIELD_PARTICIPANTS = "participants"
        const val FIELD_DATE = "date"
    }
}