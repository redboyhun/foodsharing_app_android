package com.foodsharing.app.ui.baskets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsharing.app.data.model.Basket
import com.foodsharing.app.data.repository.BasketRepository
import com.foodsharing.app.util.Resource
import kotlinx.coroutines.launch

class BasketDetailViewModel : ViewModel() {

    private val repository = BasketRepository()

    private val _basket = MutableLiveData<Resource<Basket>>()
    val basket: LiveData<Resource<Basket>> = _basket

    private val _requestState = MutableLiveData<Resource<Unit>>()
    val requestState: LiveData<Resource<Unit>> = _requestState

    fun loadBasket(basketId: Int) {
        viewModelScope.launch {
            _basket.value = Resource.Loading
            _basket.value = repository.getBasket(basketId)
        }
    }

    fun requestBasket(basketId: Int, message: String) {
        viewModelScope.launch {
            _requestState.value = Resource.Loading
            _requestState.value = repository.requestBasket(basketId, message)
        }
    }
}
