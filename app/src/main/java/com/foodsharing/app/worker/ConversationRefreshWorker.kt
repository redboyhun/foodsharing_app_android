package com.foodsharing.app.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.util.NotificationHelper
import com.foodsharing.app.util.SessionManager
import com.foodsharing.app.util.SettingsManager
import kotlinx.coroutines.flow.first

class ConversationRefreshWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val sessionManager = SessionManager(applicationContext)
        val settingsManager = SettingsManager(applicationContext)

        if (!sessionManager.isLoggedIn()) return Result.success()
        val notifyMessages = settingsManager.notifyMessagesFlow.first()
        if (!notifyMessages) return Result.success()

        return try {
            val serverUrl = settingsManager.serverUrlFlow.first()
            if (!ApiClient.baseUrl.contains(serverUrl.replace("https://", "").replace("/", ""))) {
                ApiClient.initialize(serverUrl, sessionManager)
            }

            val response = ApiClient.api.getConversations()
            if (response.code() == 403) {
                NotificationHelper.showSessionExpiredNotification(applicationContext)
                return Result.success()
            }
            if (response.isSuccessful) {
                val body = response.body()
                val conversations = body?.conversations ?: emptyList()
                val profiles = body?.profiles ?: emptyList()
                val prefs = applicationContext.getSharedPreferences("worker_state", Context.MODE_PRIVATE)

                conversations.filter { it.unreadMessages > 0 }.forEach { conv ->
                    val key = "conv_notified_${conv.id}"
                    val lastNotifiedMsg = prefs.getInt(key, -1)
                    val lastMsgId = conv.lastMessage?.id ?: -1

                    if (lastMsgId != -1 && lastMsgId != lastNotifiedMsg) {
                        val name = conv.title
                            ?: profiles.find { it.id != sessionManager.getUserId() && conv.members?.contains(it.id) == true }?.name
                            ?: "New message"
                        NotificationHelper.showNewMessageNotification(
                            applicationContext,
                            name,
                            NOTIF_ID_BASE + conv.id
                        )
                        prefs.edit().putInt(key, lastMsgId).apply()
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val NOTIF_ID_BASE = 1000
    }
}
