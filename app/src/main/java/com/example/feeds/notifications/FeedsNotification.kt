package com.example.feeds.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import com.example.feeds.ChatScreen
import com.example.feeds.R
import com.example.feeds.utilities.Utilities

class FeedsNotification(val context: Context) {
    private val utilities = Utilities()
    private val channelId = utilities.generateUniqueId()

    private val intent = Intent(context, ChatScreen::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    private val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    private val user = Person.Builder().setName("Funky").build()

    private var builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.feeds_mobile_icon)
        .setStyle(NotificationCompat.MessagingStyle(user).addMessage("Hello", 12, user))
        .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(channelId, "Feeds", importance).apply {

            }

            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    fun sendNotification(notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder)
    }
}