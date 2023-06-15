package com.example.pointiki.models

import com.example.pointiki.data.source.FirebaseVisitsDataSource.Companion.FIELD_DATE
import com.example.pointiki.data.source.FirebaseVisitsDataSource.Companion.FIELD_EVENT_ID
import com.example.pointiki.data.source.FirebaseVisitsDataSource.Companion.FIELD_ID
import com.example.pointiki.data.source.FirebaseVisitsDataSource.Companion.FIELD_PARTICIPANTS
import java.util.Date
import java.util.UUID

data class VisitEventModel(
    val id: UUID,
    val eventId: UUID,
    var participants: List<EventParticipantModel>
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            FIELD_ID to id.toString(),
            FIELD_EVENT_ID to eventId.toString(),
            FIELD_PARTICIPANTS to participants.map { participant ->
                mapOf(
                    FIELD_ID to participant.id.toString(),
                    FIELD_DATE to participant.date
                )
            }
        )
    }
}

data class EventParticipantModel(val id: UUID, val date: Date)