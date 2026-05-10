package com.foodsharing.app.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.data.model.Profile
import com.foodsharing.app.data.repository.AuthRepository
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.SessionManager
import com.foodsharing.app.util.httpErrorMessage
import com.foodsharing.app.worker.WorkScheduler
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val authRepository = AuthRepository(sessionManager)

    private val _profile = MutableLiveData<Resource<Profile>>()
    val profile: LiveData<Resource<Profile>> = _profile

    private val _logoutState = MutableLiveData<Resource<Unit>>()
    val logoutState: LiveData<Resource<Unit>> = _logoutState

    fun loadProfile() {
        viewModelScope.launch {
            _profile.value = Resource.Loading
            try {
                val response = ApiClient.api.getUser()
                _profile.value = if (response.isSuccessful && response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Error(httpErrorMessage(response.code()))
                }
            } catch (e: Exception) {
                _profile.value = Resource.Error(e.message ?: "Network error")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = Resource.Loading
            authRepository.logout()
            sessionManager.clearSession()
            WorkScheduler.cancelAll(getApplication())
            _logoutState.value = Resource.Success(Unit)
        }
    }
}
