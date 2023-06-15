package com.example.pointiki.domain.repo

import com.example.pointiki.models.EventModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface EventsRepository {
    fun getEvents(organizationId: String): Flow<List<EventModel>>
    fun getEvent(organizationId: String, eventId: UUID): Flow<EventModel>
}