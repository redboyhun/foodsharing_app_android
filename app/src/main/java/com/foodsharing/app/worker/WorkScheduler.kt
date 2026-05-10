package com.foodsharing.app.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkScheduler {

    private const val TAG_CONVERSATION = "conversation_refresh"
    private const val TAG_BASKET = "basket_refresh"
    private const val TAG_PICKUP = "pickup_reminder"

    fun scheduleAll(context: Context, intervalMinutes: Int) {
        scheduleConversationRefresh(context, intervalMinutes)
        scheduleBasketRefresh(context, intervalMinutes)
        schedulePickupReminder(context, intervalMinutes)
    }

    fun scheduleConversationRefresh(context: Context, intervalMinutes: Int) {
        val request = PeriodicWorkRequestBuilder<ConversationRefreshWorker>(
            intervalMinutes.toLong(), TimeUnit.MINUTES
        )
            .addTag(TAG_CONVERSATION)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            TAG_CONVERSATION,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun scheduleBasketRefresh(context: Context, intervalMinutes: Int) {
        val request = PeriodicWorkRequestBuilder<BasketRefreshWorker>(
            intervalMinutes.toLong(), TimeUnit.MINUTES
        )
            .addTag(TAG_BASKET)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            TAG_BASKET,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun schedulePickupReminder(context: Context, intervalMinutes: Int) {
        val request = PeriodicWorkRequestBuilder<PickupReminderWorker>(
            intervalMinutes.toLong(), TimeUnit.MINUTES
        )
            .addTag(TAG_PICKUP)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            TAG_PICKUP,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelAll(context: Context) {
        WorkManager.getInstance(context).apply {
            cancelAllWorkByTag(TAG_CONVERSATION)
            cancelAllWorkByTag(TAG_BASKET)
            cancelAllWorkByTag(TAG_PICKUP)
        }
    }
}
