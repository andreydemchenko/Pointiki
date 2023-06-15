package com.example.pointiki.data.source

import com.example.pointiki.models.VisitEventModel
import com.example.pointiki.utils.Result
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface VisitsDataSource {
    suspend fun getVisitEventModel(organizationId: String, eventId: UUID): VisitEventModel?
    suspend fun setVisit(organizationId: String, userId: UUID, eventId: UUID): Result<Unit>
    suspend fun setPointsAndVisit(organizationId: String, userId: UUID, eventId: UUID, points: Int): Result<Unit>
}