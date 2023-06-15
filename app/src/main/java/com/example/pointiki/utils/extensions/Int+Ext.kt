package com.example.pointiki.utils.extensions

fun Int.toDurationString(): String {
    val hours = this / 60
    val remainingMinutes = this % 60

    val hoursString = if (hours > 0) "$hours h" else ""
    val minutesString = if (remainingMinutes > 0) "$remainingMinutes min" else ""

    return when {
        hours > 0 && remainingMinutes > 0 -> "$hoursString $minutesString"
        hours > 0 -> hoursString
        remainingMinutes > 0 -> minutesString
        else -> "0 min"
    }
}
