package com.foodsharing.app.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.util.NotificationHelper
import com.foodsharing.app.util.SessionManager
import com.foodsharing.app.util.SettingsManager
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class PickupReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val sessionManager = SessionManager(applicationContext)
        val settingsManager = SettingsManager(applicationContext)

        if (!sessionManager.isLoggedIn()) return Result.success()
        val notifyPickups = settingsManager.notifyPickupsFlow.first()
        if (!notifyPickups) return Result.success()

        return try {
            val serverUrl = settingsManager.serverUrlFlow.first()
            if (!ApiClient.baseUrl.contains(serverUrl.replace("https://", "").replace("/", ""))) {
                ApiClient.initialize(serverUrl, sessionManager)
            }

            val response = ApiClient.api.getRegisteredPickups()
            if (response.code() == 403) {
                NotificationHelper.showSessionExpiredNotification(applicationContext)
                return Result.success()
            }
            if (!response.isSuccessful) return Result.success()

            val pickups = response.body() ?: return Result.success()
            val prefs = applicationContext.getSharedPreferences("worker_state", Context.MODE_PRIVATE)
            val now = LocalDateTime.now()

            pickups.forEach { pickup ->
                val pickupTime = parsePickupDate(pickup.date) ?: return@forEach
                val minutesUntil = ChronoUnit.MINUTES.between(now, pickupTime)
                val storeName = pickup.store.name
                val storeId = pickup.store.id

                // Notify for same-day pickups (within 24h, not yet past)
                if (minutesUntil in 0..1440) {
                    val sameDayKey = "notif_sameday_${storeId}_${pickup.date}"
                    if (!prefs.getBoolean(sameDayKey, false)) {
                        NotificationHelper.showPickupReminderNotification(
                            applicationContext,
                            storeName,
                            "today",
                            NOTIF_ID_BASE + storeId
                        )
                        prefs.edit().putBoolean(sameDayKey, true).apply()
                    }
                }

                // Notify for pickups within ~1 hour (30-90 minutes)
                if (minutesUntil in 30..90) {
                    val hourKey = "notif_1h_${storeId}_${pickup.date}"
                    if (!prefs.getBoolean(hourKey, false)) {
                        NotificationHelper.showPickupReminderNotification(
                            applicationContext,
                            storeName,
                            "in ~1 hour",
                            NOTIF_ID_BASE + 10000 + storeId
                        )
                        prefs.edit().putBoolean(hourKey, true).apply()
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun parsePickupDate(dateStr: String): LocalDateTime? {
        val formatters = listOf(
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        )
        for (fmt in formatters) {
            try {
                return LocalDateTime.parse(dateStr, fmt)
            } catch (_: Exception) {}
        }
        return null
    }

    companion object {
        private const val NOTIF_ID_BASE = 5000
    }
}
