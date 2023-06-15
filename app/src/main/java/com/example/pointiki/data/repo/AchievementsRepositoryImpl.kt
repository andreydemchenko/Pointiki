package com.example.pointiki.data.repo

import com.example.pointiki.data.source.AchievementsDataSource
import com.example.pointiki.data.source.EventsDataSource
import com.example.pointiki.domain.repo.AchievementsRepository
import com.example.pointiki.domain.repo.EventsRepository
import com.example.pointiki.models.Achievement
import com.example.pointiki.models.EventModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AchievementsRepositoryImpl @Inject constructor(private val dataSource: AchievementsDataSource) : AchievementsRepository {

    override fun getAchievements(organizationId: String): Flow<List<Achievement>> {
        return dataSource.getAchievements(organizationId)
    }

}