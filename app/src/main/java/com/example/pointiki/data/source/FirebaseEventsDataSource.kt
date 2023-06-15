package com.example.pointiki.data.source

import android.util.Log
import com.example.pointiki.models.EventModel
import com.example.pointiki.models.PointEntry
import com.example.pointiki.models.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import java.util.concurrent.CancellationException
import javax.inject.Inject

class FirebaseEventsDataSource @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : EventsDataSource {

    override fun getEvents(organizationId: String): Flow<List<EventModel>> {
        return callbackFlow {
            val eventsCollectionRef = firebaseFirestore
                .collection(COLLECTION_DATA)
                .document(organizationId)
                .collection(COLLECTION_EVENTS)

            val listener = eventsCollectionRef
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(CancellationException("Failed to get events: ${error.message}"))
                    } else if (snapshot != null && !snapshot.isEmpty) {
                        val events = snapshot.documents.mapNotNull { doc ->
                            val id = UUID.fromString(doc.id)
                            val eventMap = doc.data
                            if (eventMap != null) {
                                val imageUrl = eventMap[FIELD_IMAGE_URL] as String
                                val name = eventMap[FIELD_NAME] as String
                                val description = eventMap[FIELD_DESCRIPTION] as String
                                val points = (eventMap[FIELD_POINTS] as Long).toInt()
                                val duration = (eventMap[FIELD_DURATION] as Long).toInt()
                                val date = (eventMap[FIELD_DATE] as Timestamp).toDate()

                                EventModel(
                                    id = id,
                                    title = name,
                                    description = description,
                                    imageUrl = imageUrl,
                                    points = points,
                                    duration = duration,
                                    date = date
                                )
                            } else {
                                null
                            }
                        }
                        trySend(events)
                    } else {
                        Log.d("FirebaseEventsDataSource", "No events found")
                    }
                }

            awaitClose { listener.remove() }
        }
    }

    override fun getEvent(organizationId: String, eventId: UUID): Flow<EventModel> {
        return callbackFlow {
            val eventDocumentRef = firebaseFirestore
                .collection(COLLECTION_DATA)
                .document(organizationId)
                .collection(COLLECTION_EVENTS)
                .document(eventId.toString())

            val listener = eventDocumentRef
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(CancellationException("Failed to get event: ${error.message}"))
                    } else if (snapshot != null && snapshot.exists()) {
                        val eventMap = snapshot.data
                        if (eventMap != null) {
                            val imageUrl = eventMap[FIELD_IMAGE_URL] as String
                            val name = eventMap[FIELD_NAME] as String
                            val description = eventMap[FIELD_DESCRIPTION] as String
                            val points = (eventMap[FIELD_POINTS] as Long).toInt()
                            val duration = (eventMap[FIELD_DURATION] as Long).toInt()
                            val date = (eventMap[FIELD_DATE] as Timestamp).toDate()

                            val event = EventModel(
                                id = eventId,
                                title = name,
                                description = description,
                                imageUrl = imageUrl,
                                points = points,
                                duration = duration,
                                date = date
                            )
                            trySend(event)
                        } else {
                            Log.d("FirebaseEventsDataSource", "Event data is null")
                        }
                    } else {
                        Log.d("FirebaseEventsDataSource", "Event not found")
                    }
                }

            awaitClose { listener.remove() }
        }
    }

    companion object {
        const val COLLECTION_DATA = "data"
        const val COLLECTION_EVENTS = "events"
        const val FIELD_IMAGE_URL = "imageUrl"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_POINTS = "points"
        const val FIELD_DURATION = "duration"
        const val FIELD_DATE = "date"
    }
}