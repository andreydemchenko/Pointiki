package com.example.pointiki.domain.repo

import com.example.pointiki.models.Achievement
import kotlinx.coroutines.flow.Flow

interface AchievementsRepository {
    fun getAchievements(organizationId: String): Flow<List<Achievement>>
}