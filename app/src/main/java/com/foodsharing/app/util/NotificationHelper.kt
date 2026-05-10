package com.foodsharing.app.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.foodsharing.app.R
import com.foodsharing.app.ui.auth.LoginActivity
import com.foodsharing.app.ui.main.MainActivity

object NotificationHelper {

    const val CHANNEL_MESSAGES = "ch_messages"
    const val CHANNEL_BASKETS = "ch_baskets"
    const val CHANNEL_PICKUPS = "ch_pickups"
    const val CHANNEL_AUTH = "ch_auth"
    private const val NOTIF_ID_SESSION_EXPIRED = 9999

    fun createChannels(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_MESSAGES,
                context.getString(R.string.notif_channel_messages),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.notif_channel_messages_desc)
            }
        )

        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_BASKETS,
                context.getString(R.string.notif_channel_baskets),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.notif_channel_baskets_desc)
            }
        )

        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_PICKUPS,
                context.getString(R.string.notif_channel_pickups),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.notif_channel_pickups_desc)
            }
        )

        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_AUTH,
                "Account",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Sign-in and session alerts"
            }
        )
    }

    fun showNewMessageNotification(context: Context, conversationName: String, notifId: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "conversations")
        }
        val pi = PendingIntent.getActivity(
            context, notifId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notif = NotificationCompat.Builder(context, CHANNEL_MESSAGES)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.notif_new_message))
            .setContentText(conversationName)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notifId, notif)
    }

    fun showNearbyBasketNotification(context: Context, description: String, notifId: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "baskets")
        }
        val pi = PendingIntent.getActivity(
            context, notifId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notif = NotificationCompat.Builder(context, CHANNEL_BASKETS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.notif_new_basket))
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notifId, notif)
    }

    fun showSessionExpiredNotification(context: Context) {
        val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("session_expired", true)
        }
        val pi = PendingIntent.getActivity(
            context, NOTIF_ID_SESSION_EXPIRED, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notif = NotificationCompat.Builder(context, CHANNEL_AUTH)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Session expired")
            .setContentText("Tap to sign in again.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID_SESSION_EXPIRED, notif)
    }

    fun showPickupReminderNotification(context: Context, storeName: String, timeLabel: String, notifId: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "pickups")
        }
        val pi = PendingIntent.getActivity(
            context, notifId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notif = NotificationCompat.Builder(context, CHANNEL_PICKUPS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.notif_pickup_reminder))
            .setContentText("$storeName – $timeLabel")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notifId, notif)
    }
}
