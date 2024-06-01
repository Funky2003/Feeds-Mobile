package com.example.feeds.utilities

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class Utilities {

    fun generateUniqueId() : String {
        return UUID.randomUUID().toString()
    }
    fun getCurrentTimeStamp() : String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return dateFormat.format(Date())
    }
}