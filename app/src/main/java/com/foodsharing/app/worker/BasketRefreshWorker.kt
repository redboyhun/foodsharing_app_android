package com.foodsharing.app.worker

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.edit
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.util.NotificationHelper
import com.foodsharing.app.util.SessionManager
import com.foodsharing.app.util.SettingsManager
import kotlinx.coroutines.flow.first

class BasketRefreshWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val sessionManager = SessionManager(applicationContext)
        val settingsManager = SettingsManager(applicationContext)

        if (!sessionManager.isLoggedIn()) return Result.success()
        val notifyBaskets = settingsManager.notifyBasketsFlow.first()
        if (!notifyBaskets) return Result.success()

        return try {
            val serverUrl = settingsManager.serverUrlFlow.first()
            if (!ApiClient.baseUrl.contains(serverUrl.replace("https://", "").replace("/", ""))) {
                ApiClient.initialize(serverUrl, sessionManager)
            }

            // Use last known location from LocationManager
            val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location: Location? = try {
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    ?: locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } catch (e: SecurityException) {
                null
            }

            location?.latitude ?: return Result.success()
            location?.longitude ?: return Result.success()

//            val response = ApiClient.api.getNearbyBaskets(lat, lon, 3)
            val response = ApiClient.api.getNearbyBaskets(3)
            if (response.code() == 403) {
                NotificationHelper.showSessionExpiredNotification(applicationContext)
                return Result.success()
            }
            if (response.isSuccessful) {
                val baskets = response.body() ?: emptyList()
                val prefs = applicationContext.getSharedPreferences("worker_state", Context.MODE_PRIVATE)
                val knownIds = prefs.getStringSet("known_basket_ids", emptySet()) ?: emptySet()
                val newBaskets = baskets.filter { it.id.toString() !in knownIds }

                newBaskets.take(3).forEach { basket ->
                    NotificationHelper.showNearbyBasketNotification(
                        applicationContext,
                        basket.description.take(60),
                        NOTIF_ID_BASE + basket.id
                    )
                }

                if (newBaskets.isNotEmpty()) {
                    val updatedIds = knownIds.toMutableSet()
                    updatedIds.addAll(baskets.map { it.id.toString() })
                    // Keep set bounded
                    if (updatedIds.size > 500) {
                        val toRemove = updatedIds.take(updatedIds.size - 500)
                        updatedIds.removeAll(toRemove.toSet())
                    }
                    prefs.edit {
                        putStringSet("known_basket_ids", updatedIds)
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val NOTIF_ID_BASE = 3000
    }
}
