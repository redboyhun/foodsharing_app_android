package com.foodsharing.app.data.repository

import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.data.model.*
import com.foodsharing.app.util.Resource

class PickupRepository {

    suspend fun getPickupOptions(): Resource<List<PickupOption>> {
        return try {
            val response = ApiClient.api.getPickupOptions()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun getRegisteredPickups(): Resource<List<RegisteredPickup>> {
        return try {
            val response = ApiClient.api.getRegisteredPickups()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun joinPickup(storeId: Int, pickupDate: String): Resource<Unit> {
        return try {
            val response = ApiClient.api.joinPickup(storeId, pickupDate)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun leavePickup(storeId: Int, pickupDate: String): Resource<Unit> {
        return try {
            val response = ApiClient.api.leavePickup(storeId, pickupDate)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun getUserStores(): Resource<List<StoreInfo>> {
        return try {
            val response = ApiClient.api.getUserStores()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }
}
