package com.tebet.mojual.common.util

import android.text.format.DateFormat
import com.tebet.mojual.R
import java.text.SimpleDateFormat
import java.util.*

fun Long.toDisplayFullDate(): String {
    if (this <= 0) {
        return ""
    }

    val smsTime = Calendar.getInstance()
    smsTime.timeInMillis = this

    val now = Calendar.getInstance()

    val timeFormatString = "h:mm aa"
    val dateTimeFormatString = "dd MMM, yyyy"
    return if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
        Utility.getInstance().getString(R.string.message_today) + " " + DateFormat.format(
            timeFormatString,
            smsTime
        )
    } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
        Utility.getInstance().getString(R.string.message_yesterday) + " " + DateFormat.format(
            timeFormatString,
            smsTime
        )
    } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
        DateFormat.format(dateTimeFormatString, smsTime).toString()
    } else {
        DateFormat.format("dd MMM, yyyy", smsTime).toString()
    }
}

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