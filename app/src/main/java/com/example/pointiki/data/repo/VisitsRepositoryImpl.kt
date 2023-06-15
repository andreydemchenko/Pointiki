package com.example.pointiki.data.repo

import com.example.pointiki.data.source.VisitsDataSource
import com.example.pointiki.domain.repo.VisitsRepository
import com.example.pointiki.models.VisitEventModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import com.example.pointiki.utils.Result
import javax.inject.Inject

class VisitsRepositoryImpl @Inject constructor(private val dataSource: VisitsDataSource) :
    VisitsRepository {

    override suspend fun getVisitEventModel(organizationId: String, eventId: UUID): VisitEventModel? {
        return dataSource.getVisitEventModel(organizationId, eventId)
    }

    override suspend fun setVisit(organizationId: String, userId: UUID, eventId: UUID): Result<Unit> {
        return dataSource.setVisit(organizationId, userId, eventId)
    }

    override suspend fun setPointsAndVisit(
        organizationId: String,
        userId: UUID,
        eventId: UUID,
        points: Int
    ): Result<Unit> {
        return dataSource.setPointsAndVisit(organizationId, userId, eventId, points)
    }

}