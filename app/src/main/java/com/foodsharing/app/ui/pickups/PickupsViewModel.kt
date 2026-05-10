package com.foodsharing.app.ui.pickups

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.foodsharing.app.data.model.PickupOption
import com.foodsharing.app.data.repository.PickupRepository
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.SessionManager
import kotlinx.coroutines.launch

class PickupsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PickupRepository()
    private val sessionManager = SessionManager(application)

    private val _options = MutableLiveData<Resource<List<PickupOption>>>()
    val options: LiveData<Resource<List<PickupOption>>> = _options

    private val _registered = MutableLiveData<Resource<List<PickupOption>>>()
    val registered: LiveData<Resource<List<PickupOption>>> = _registered

    private val _joinState = MutableLiveData<Resource<Unit>>()
    val joinState: LiveData<Resource<Unit>> = _joinState

    fun loadPickupOptions() {
        viewModelScope.launch {
            _options.value = Resource.Loading
            _options.value = repository.getPickupOptions()
        }
    }

    fun loadRegisteredPickups() {
        viewModelScope.launch {
            _registered.value = Resource.Loading
            _registered.value = repository.getRegisteredPickups(sessionManager.getUserId())
        }
    }

    fun joinPickup(storeId: Int, pickupDate: String) {
        viewModelScope.launch {
            _joinState.value = Resource.Loading
            val result = repository.joinPickup(storeId, pickupDate)
            _joinState.value = result
            if (result is Resource.Success) {
                loadPickupOptions()
                loadRegisteredPickups()
            }
        }
    }

    fun leavePickup(storeId: Int, pickupDate: String) {
        viewModelScope.launch {
            _joinState.value = Resource.Loading
            val result = repository.leavePickup(storeId, pickupDate)
            _joinState.value = result
            if (result is Resource.Success) {
                loadPickupOptions()
                loadRegisteredPickups()
            }
        }
    }
}
