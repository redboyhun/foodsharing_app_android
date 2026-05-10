package com.foodsharing.app.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.foodsharing.app.util.SettingsManager
import com.foodsharing.app.worker.WorkScheduler
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsManager = SettingsManager(application)

    val refreshInterval: LiveData<Int> = settingsManager.refreshIntervalFlow.asLiveData()
    val notifyMessages: LiveData<Boolean> = settingsManager.notifyMessagesFlow.asLiveData()
    val notifyBaskets: LiveData<Boolean> = settingsManager.notifyBasketsFlow.asLiveData()
    val notifyPickups: LiveData<Boolean> = settingsManager.notifyPickupsFlow.asLiveData()
    val serverUrl: LiveData<String> = settingsManager.serverUrlFlow.asLiveData()
    val basketDistance: LiveData<Int> = settingsManager.basketDistanceFlow.asLiveData()

    private val _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean> = _saved

    fun saveSettings(
        intervalMinutes: Int,
        notifyMessages: Boolean,
        notifyBaskets: Boolean,
        notifyPickups: Boolean,
        serverUrl: String,
        basketDistance: Int
    ) {
        viewModelScope.launch {
            settingsManager.setRefreshInterval(intervalMinutes)
            settingsManager.setNotifyMessages(notifyMessages)
            settingsManager.setNotifyBaskets(notifyBaskets)
            settingsManager.setNotifyPickups(notifyPickups)
            settingsManager.setBasketDistance(basketDistance)
            val currentUrl = this@SettingsViewModel.serverUrl.value
            if (currentUrl != serverUrl) {
                settingsManager.setServerUrl(serverUrl)
            }
            WorkScheduler.scheduleAll(getApplication(), intervalMinutes)
            _saved.value = true
        }
    }
}
