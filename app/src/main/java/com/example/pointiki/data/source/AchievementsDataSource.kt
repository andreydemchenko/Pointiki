package com.example.pointiki.data.source

import com.example.pointiki.models.Achievement
import kotlinx.coroutines.flow.Flow

interface AchievementsDataSource {
    fun getAchievements(organizationId: String): Flow<List<Achievement>>
}