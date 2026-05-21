package com.foodsharing.app.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
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
                context.getString(R.string.notif_channel_auth),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.notif_channel_auth_desc)
            }
        )
    }

    @SuppressLint("MissingPermission")
    fun showNewMessageNotification(
        context: Context,
        conversationName: String,
        notifId: Int,
        messagePreview: String? = null
    ) {
        if (!hasNotificationPermission(context)) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "conversations")
        }
        val pi = PendingIntent.getActivity(
            context, notifId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, CHANNEL_MESSAGES)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(conversationName)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pi)
            .setAutoCancel(true)
        
        if (messagePreview != null) {
            builder.setContentText(messagePreview)
        } else {
            builder.setContentText(context.getString(R.string.notif_new_message))
        }

        NotificationManagerCompat.from(context).notify(notifId, builder.build())
    }

    @SuppressLint("MissingPermission")
    fun showNearbyBasketNotification(context: Context, description: String, notifId: Int) {
        if (!hasNotificationPermission(context)) return

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

    @SuppressLint("MissingPermission")
    fun showSessionExpiredNotification(context: Context) {
        if (!hasNotificationPermission(context)) return

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
            .setContentTitle(context.getString(R.string.notif_session_expired))
            .setContentText(context.getString(R.string.notif_session_expired_desc))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID_SESSION_EXPIRED, notif)
    }

    @SuppressLint("MissingPermission")
    fun showPickupReminderNotification(context: Context, storeName: String, timeLabel: String, notifId: Int) {
        if (!hasNotificationPermission(context)) return

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

    private fun hasNotificationPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }
}
