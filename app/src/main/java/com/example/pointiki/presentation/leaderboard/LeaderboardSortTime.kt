package com.example.pointiki.presentation.leaderboard

enum class LeaderboardSortTime(val value: String) {
    WEEK("This week"),
    MONTH("This month"),
    ALL_TIME("All time");

    companion object {
        val values = values()
    }
}