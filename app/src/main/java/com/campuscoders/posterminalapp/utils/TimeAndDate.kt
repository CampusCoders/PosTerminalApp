package com.campuscoders.posterminalapp.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TimeAndDate {
    companion object {
        fun getLocalDate(dateFormat: String): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Android 8.0 (API 26) ve üstü
                val currentDate = java.time.LocalDate.now()
                val formatter =
                    java.time.format.DateTimeFormatter.ofPattern(dateFormat, Locale.getDefault())
                return currentDate.format(formatter)
            } else {
                // Android 7 (API 24) ve altı
                val currentDate = Date()
                val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
                return formatter.format(currentDate)
            }
        }

        fun getTime(): String {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            return "$hour:$minute"
        }
    }
}