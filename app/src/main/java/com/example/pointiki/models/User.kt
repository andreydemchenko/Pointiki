package com.example.pointiki.models

import java.util.Date
import java.util.UUID

data class User(
    val id: UUID,
    val icon: Int,
    val name: String,
    var points: Int,
    val pointsHistory: List<PointEntry>
)

data class UserModel(
    val id: UUID,
    val imageUrl: String,
    val name: String,
    val phone: String,
    var points: Int,
    var pointsHistory: List<PointEntry>,
    val achievements: List<AchievementUser>,
    val progresses: List<CurrentProgressUser>
)

data class PointEntry(val points: Int, val date: Date)

data class AchievementUser(val id: UUID, val date: Date)

data class CurrentProgressUser(
    val id: UUID,
    val progress: Int,
    val type: AchievementType
)