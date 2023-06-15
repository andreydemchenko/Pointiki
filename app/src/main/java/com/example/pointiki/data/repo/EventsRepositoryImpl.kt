package com.example.pointiki.data.repo

import com.example.pointiki.data.source.EventsDataSource
import com.example.pointiki.domain.repo.EventsRepository
import com.example.pointiki.models.EventModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(private val dataSource: EventsDataSource) : EventsRepository {

    override fun getEvents(organizationId: String): Flow<List<EventModel>> {
        return dataSource.getEvents(organizationId)
    }

    override fun getEvent(organizationId: String, eventId: UUID): Flow<EventModel> {
        return dataSource.getEvent(organizationId, eventId)
    }

}