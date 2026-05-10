package com.foodsharing.app.ui.baskets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsharing.app.data.model.Basket
import com.foodsharing.app.data.model.CreateBasketRequest
import com.foodsharing.app.data.model.UpdateBasketRequest
import com.foodsharing.app.data.repository.BasketRepository
import com.foodsharing.app.util.Resource
import kotlinx.coroutines.launch

class AddEditBasketViewModel : ViewModel() {

    private val repository = BasketRepository()

    private val _saveState = MutableLiveData<Resource<Basket>>()
    val saveState: LiveData<Resource<Basket>> = _saveState

    fun createBasket(description: String, lifetimeInDays: Int, weightInGrams: Int) {
        viewModelScope.launch {
            _saveState.value = Resource.Loading
            _saveState.value = repository.createBasket(
                CreateBasketRequest(
                    description = description,
                    lifetimeInDays = lifetimeInDays,
                    weightInGrams = weightInGrams
                )
            )
        }
    }

    fun updateBasket(basketId: Int, description: String, lifetimeInDays: Int?, weightInGrams: Int?) {
        viewModelScope.launch {
            _saveState.value = Resource.Loading
            _saveState.value = repository.updateBasket(
                basketId,
                UpdateBasketRequest(
                    description = description,
                    lifetimeInDays = lifetimeInDays,
                    weight = weightInGrams?.toDouble()
                )
            )
        }
    }
}
