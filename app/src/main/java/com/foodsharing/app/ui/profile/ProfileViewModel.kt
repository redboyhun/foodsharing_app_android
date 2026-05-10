package com.foodsharing.app.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.foodsharing.app.data.model.UserDetails
import com.foodsharing.app.data.repository.AuthRepository
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.SessionManager
import com.foodsharing.app.worker.WorkScheduler
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)
    private val authRepository = AuthRepository(sessionManager)

    private val _profile = MutableLiveData<Resource<UserDetails>>()
    val profile: LiveData<Resource<UserDetails>> = _profile

    private val _logoutState = MutableLiveData<Resource<Unit>>()
    val logoutState: LiveData<Resource<Unit>> = _logoutState

    fun loadProfile() {
        viewModelScope.launch {
            _profile.value = Resource.Loading
            _profile.value = authRepository.getCurrentUser()
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
