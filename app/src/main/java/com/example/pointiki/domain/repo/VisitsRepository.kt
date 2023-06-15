package com.example.pointiki.domain.repo

import com.example.pointiki.models.VisitEventModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import com.example.pointiki.utils.Result

interface VisitsRepository {
    suspend fun getVisitEventModel(organizationId: String, eventId: UUID): VisitEventModel?
    suspend fun setVisit(organizationId: String, userId: UUID, eventId: UUID): Result<Unit>
    suspend fun setPointsAndVisit(organizationId: String, userId: UUID, eventId: UUID, points: Int): Result<Unit>
}