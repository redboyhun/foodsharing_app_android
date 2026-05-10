package com.foodsharing.app.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.data.repository.AuthRepository
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.SessionManager
import com.foodsharing.app.util.SettingsManager
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val repository = AuthRepository(sessionManager)
    private val settingsManager = SettingsManager(application)

    private val _loginState = MutableLiveData<Resource<Unit>>()
    val loginState: LiveData<Resource<Unit>> = _loginState

    fun login(email: String, password: String, serverUrl: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            settingsManager.setServerUrl(serverUrl)
            ApiClient.reinitialize(serverUrl)
            val result = repository.login(email, password)
            if (result is Resource.Success) {
                val userResult = repository.getCurrentUser()
                if (userResult is Resource.Success) {
                    val user = userResult.data
                    sessionManager.saveUserId(user.id)
                    sessionManager.saveUserName(user.name)
                    sessionManager.saveUserAvatar(user.avatar)
                }
            }
            _loginState.value = result
        }
    }
}
