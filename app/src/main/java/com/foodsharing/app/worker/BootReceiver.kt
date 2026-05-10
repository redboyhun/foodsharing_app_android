package com.foodsharing.app.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.foodsharing.app.util.SessionManager
import com.foodsharing.app.util.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val sessionManager = SessionManager(context)
        if (!sessionManager.isLoggedIn()) return

        CoroutineScope(Dispatchers.IO).launch {
            val settingsManager = SettingsManager(context)
            val interval = settingsManager.refreshIntervalFlow.first()
            WorkScheduler.scheduleAll(context, interval)
        }
    }
}
