package com.foodsharing.app.data.repository

import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.data.model.*
import com.foodsharing.app.util.Resource

class BasketRepository {

    suspend fun getNearbyBaskets(lat: Double, lon: Double, distance: Int = 3): Resource<List<Basket>> {
        return try {
            val response = ApiClient.api.getNearbyBaskets(distance)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun getMyBaskets(): Resource<List<Basket>> {
        return try {
            val response = ApiClient.api.getMyBaskets()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun getBasket(basketId: Int): Resource<Basket> {
        return try {
            val response = ApiClient.api.getBasket(basketId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun createBasket(request: CreateBasketRequest): Resource<Basket> {
        return try {
            val response = ApiClient.api.createBasket(request)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun updateBasket(basketId: Int, request: UpdateBasketRequest): Resource<Basket> {
        return try {
            val response = ApiClient.api.updateBasket(basketId, request)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun deleteBasket(basketId: Int): Resource<Unit> {
        return try {
            val response = ApiClient.api.deleteBasket(basketId)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun requestBasket(basketId: Int, message: String): Resource<Unit> {
        return try {
            val response = ApiClient.api.requestBasket(basketId, OptionalMessage(message))
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }
}
