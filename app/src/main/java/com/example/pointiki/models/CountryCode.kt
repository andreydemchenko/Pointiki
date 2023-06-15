package com.example.pointiki.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryCode(
    val country: String,
    val code: String
): Parcelable
