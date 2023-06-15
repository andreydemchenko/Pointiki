package com.example.pointiki.models

import java.util.UUID

data class Achievement(
    val id: UUID,
    val imageUrl: String,
    val name: String,
    val points: Int,
    val commitment: Int,
    val type: AchievementType,
    val order: Int
)

enum class AchievementType {
    GAINING_POINTS,
    VISITS,
    OTHER
}