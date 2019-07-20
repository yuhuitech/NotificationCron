package com.github.notificationcron.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.github.notificationcron.R
import java.util.concurrent.atomic.AtomicInteger

private const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
private const val INITIAL_NOTIFICATION_ID = 5000
private val NOTIFICATION_ID_COUNTER = AtomicInteger(INITIAL_NOTIFICATION_ID)

private fun newNotificationId(): Int {
    while (true) {
        val existingValue = NOTIFICATION_ID_COUNTER.get()
        val newValue = existingValue + 1
        if (NOTIFICATION_ID_COUNTER.compareAndSet(existingValue, newValue)) {
            return newValue
        }
    }
}

fun createNotificationChannel(context: Context, notificationManager: NotificationManager) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.notification_channel_name)
        val descriptionText = context.getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }
}

fun createNotification(context: Context, title: String, text: String): Notification {
    val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setContentText(text)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    return builder.build()
}

fun showNotification(context: Context, title: String, text: String) {
    val notification = createNotification(context, title, text)
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    createNotificationChannel(context, notificationManager)
    notificationManager.notify(newNotificationId(), notification)
}