package com.example.pointiki.data.repo

import com.example.pointiki.data.source.UserDataSource
import com.example.pointiki.domain.repo.UserRepository
import com.example.pointiki.models.Achievement
import com.example.pointiki.models.AchievementType
import com.example.pointiki.models.UserModel
import kotlinx.coroutines.flow.Flow
import com.example.pointiki.utils.Result
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val dataSource: UserDataSource) :
    UserRepository {

    override suspend fun createUser(organizationId: String, user: UserModel): Result<Unit> {
        return dataSource.createUser(organizationId, user)
    }

    override fun getUser(organizationId: String, userId: UUID): Flow<UserModel> {
        return dataSource.getUser(organizationId, userId)
    }

    override fun getAllUsers(organizationId: String): Flow<List<UserModel>> {
        return dataSource.getAllUsers(organizationId)
    }

    override suspend fun setNextAchievementProgress(organizationId: String, userId: UUID, newId: UUID, achievement: Achievement): Result<Unit> {
        return dataSource.setNextAchievementProgress(organizationId, userId, newId, achievement)
    }

}