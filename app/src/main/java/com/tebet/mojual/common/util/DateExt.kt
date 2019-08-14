package com.tebet.mojual.common.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDisplayDate(): String {
    if (this <= 0) {
        return ""
    }
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return formatter.format(calendar.time)
}


fun Long.toDisplayDateShort(): String {
    if (this <= 0) {
        return ""
    }
    val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return formatter.format(calendar.time)
}