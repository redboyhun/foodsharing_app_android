package com.foodsharing.app

import android.app.Application
import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.util.NotificationHelper
import com.foodsharing.app.util.SessionManager
import com.foodsharing.app.util.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FoodsharingApplication : Application() {

    lateinit var sessionManager: SessionManager
    lateinit var settingsManager: SettingsManager

    override fun onCreate() {
        super.onCreate()
        sessionManager = SessionManager(this)
        settingsManager = SettingsManager(this)
        NotificationHelper.createChannels(this)
        initApiClient()
    }

    private fun initApiClient() {
        CoroutineScope(Dispatchers.IO).launch {
            val serverUrl = settingsManager.serverUrlFlow.first()
            ApiClient.initialize(serverUrl, sessionManager)
        }
    }
}
