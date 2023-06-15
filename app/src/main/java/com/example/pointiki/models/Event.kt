package com.example.pointiki.models

import java.util.Date
import java.util.UUID

data class Event(
    val id: Int,
    val title: String,
    val date: Date,
    val imageResource: Int
)

data class EventModel(
    val id: UUID,
    val title: String,
    val description: String,
    val points: Int,
    val duration: Int,
    val date: Date,
    val imageUrl: String
)
