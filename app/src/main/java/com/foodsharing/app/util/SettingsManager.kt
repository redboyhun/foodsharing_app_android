package com.foodsharing.app.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    private object Keys {
        val REFRESH_INTERVAL = intPreferencesKey("refresh_interval_minutes")
        val NOTIFY_MESSAGES = booleanPreferencesKey("notify_messages")
        val NOTIFY_BASKETS = booleanPreferencesKey("notify_baskets")
        val NOTIFY_PICKUPS = booleanPreferencesKey("notify_pickups")
        val SERVER_URL = stringPreferencesKey("server_url")
        val BASKET_DISTANCE = intPreferencesKey("basket_distance")
    }

    val refreshIntervalFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[Keys.REFRESH_INTERVAL] ?: DEFAULT_REFRESH_INTERVAL
    }

    val notifyMessagesFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.NOTIFY_MESSAGES] ?: true
    }

    val notifyBasketsFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.NOTIFY_BASKETS] ?: true
    }

    val notifyPickupsFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.NOTIFY_PICKUPS] ?: true
    }

    val serverUrlFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.SERVER_URL] ?: DEFAULT_SERVER_URL
    }

    val basketDistanceFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[Keys.BASKET_DISTANCE] ?: DEFAULT_BASKET_DISTANCE
    }

    suspend fun setRefreshInterval(minutes: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.REFRESH_INTERVAL] = minutes
        }
    }

    suspend fun setNotifyMessages(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NOTIFY_MESSAGES] = enabled
        }
    }

    suspend fun setNotifyBaskets(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NOTIFY_BASKETS] = enabled
        }
    }

    suspend fun setNotifyPickups(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NOTIFY_PICKUPS] = enabled
        }
    }

    suspend fun setServerUrl(url: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SERVER_URL] = url
        }
    }

    suspend fun setBasketDistance(distance: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.BASKET_DISTANCE] = distance
        }
    }

    companion object {
        const val DEFAULT_REFRESH_INTERVAL = 15
        const val DEFAULT_SERVER_URL = "https://foodsharing.de/"
        const val BETA_SERVER_URL = "https://beta.foodsharing.de/"
        const val DEFAULT_BASKET_DISTANCE = 30
    }
}
