package com.foodsharing.app.ui.baskets

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.foodsharing.app.data.model.Basket
import com.foodsharing.app.data.repository.BasketRepository
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.SettingsManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NearbyBasketsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BasketRepository()
    private val settingsManager = SettingsManager(application)

    private val _baskets = MutableLiveData<Resource<List<Basket>>>()
    val baskets: LiveData<Resource<List<Basket>>> = _baskets

    fun loadNearbyBaskets(lat: Double, lon: Double) {
        viewModelScope.launch {
            _baskets.value = Resource.Loading
            val distance = settingsManager.basketDistanceFlow.first()
            _baskets.value = repository.getNearbyBaskets(lat, lon, distance)
        }
    }
}
