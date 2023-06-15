package com.example.pointiki.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Organization(
    val id: UUID,
    val name: String,
    val imageUrl: String
) : Parcelable