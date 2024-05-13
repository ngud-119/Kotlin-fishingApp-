package com.harissabil.fisch.core.common.util

import android.content.Context
import com.google.firebase.Timestamp
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

fun Timestamp.toDateAtTime(context: Context): String {
    val is24HourFormat = android.text.format.DateFormat.is24HourFormat(context)

    val date = this.toDate()

    val dateString: String = try {
        val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
        val timeFormatter = if (is24HourFormat) {
            SimpleDateFormat("HH:mm", Locale.getDefault())
        } else {
            SimpleDateFormat("hh:mm a", Locale.getDefault())
        }

        val dateString = dateFormatter.format(date)
        val timeString = timeFormatter.format(date)

        "$dateString @ $timeString"
    } catch (e: ParseException) {
        val sdf = if (is24HourFormat) {
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        } else {
            SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        }
        sdf.format(date)
    }

    return dateString
}

fun Timestamp.toDateYyyyMmDd(): String {
    val date = this.toDate()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(date)
}

fun Timestamp.toTime(context: Context): String {
    val is24HourFormat = android.text.format.DateFormat.is24HourFormat(context)
    val date = this.toDate()
    val sdf = if (is24HourFormat) {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    } else {
        SimpleDateFormat("hh:mm a", Locale.getDefault())
    }
    return sdf.format(date)
}

fun Long.toDateYyyyMmDd(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(this)
}

fun String.toTimestamp(context: Context): Timestamp {
    val is24HourFormat = android.text.format.DateFormat.is24HourFormat(context)
    val sdf = if (is24HourFormat) {
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    } else {
        SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
    }
    val date = sdf.parse(this)
    return Timestamp(date!!)
}