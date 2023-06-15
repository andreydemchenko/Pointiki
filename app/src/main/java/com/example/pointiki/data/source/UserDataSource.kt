package com.example.pointiki.data.source

import com.example.pointiki.models.Achievement
import com.example.pointiki.models.AchievementType
import com.example.pointiki.models.UserModel
import com.example.pointiki.utils.Result
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface UserDataSource {
    suspend fun createUser(organizationId: String, user: UserModel): Result<Unit>
    fun getUser(organizationId: String, userId: UUID): Flow<UserModel>
    fun getAllUsers(organizationId: String): Flow<List<UserModel>>
    suspend fun setNextAchievementProgress(
        organizationId: String,
        userId: UUID,
        newId: UUID,
        achievement: Achievement
    ) : Result<Unit>
}