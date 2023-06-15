package com.example.pointiki.data.source

import com.example.pointiki.models.EventModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface EventsDataSource {
    fun getEvents(organizationId: String): Flow<List<EventModel>>
    fun getEvent(organizationId: String, eventId: UUID): Flow<EventModel>
}