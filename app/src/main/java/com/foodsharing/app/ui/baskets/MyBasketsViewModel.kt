package com.foodsharing.app.ui.baskets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsharing.app.data.model.Basket
import com.foodsharing.app.data.repository.BasketRepository
import com.foodsharing.app.util.Resource
import kotlinx.coroutines.launch

class MyBasketsViewModel : ViewModel() {

    private val repository = BasketRepository()

    private val _baskets = MutableLiveData<Resource<List<Basket>>>()
    val baskets: LiveData<Resource<List<Basket>>> = _baskets

    private val _deleteState = MutableLiveData<Resource<Unit>>()
    val deleteState: LiveData<Resource<Unit>> = _deleteState

    fun loadMyBaskets() {
        _baskets.value = Resource.Loading
        viewModelScope.launch {
            _baskets.value = repository.getMyBaskets()
        }
    }

    fun deleteBasket(basketId: Int) {
        _deleteState.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.deleteBasket(basketId)
            _deleteState.value = result
            if (result is Resource.Success) {
                loadMyBaskets()
            }
        }
    }
}
