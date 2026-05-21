package com.foodsharing.app.data.repository

import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.data.model.*
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.httpErrorMessage
import com.foodsharing.app.util.toUtcIsoString

class PickupRepository {

    suspend fun getPickupOptions(): Resource<List<PickupOption>> {
        return try {
            val response = ApiClient.api.getPickupOptions()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(httpErrorMessage(response.code()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun getRegisteredPickups(userId: Int): Resource<List<PickupOption>> {
        return try {
            val response = ApiClient.api.getRegisteredPickups(userId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(httpErrorMessage(response.code()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun joinPickup(storeId: Int, pickupDate: String): Resource<Unit> {
        return try {
            val utcDate = toUtcIsoString(pickupDate)
            val response = ApiClient.api.joinPickup(storeId, utcDate)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(httpErrorMessage(response.code()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun leavePickup(storeId: Int, pickupDate: String, userId: Int): Resource<Unit> {
        return try {
            val utcDate = toUtcIsoString(pickupDate)
            val response = ApiClient.api.leavePickup(storeId, utcDate, userId)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(httpErrorMessage(response.code()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun getUserStores(userId: Int): Resource<List<StoreInfo>> {
        return try {
            val response = ApiClient.api.getUserStores(userId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(httpErrorMessage(response.code()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }
}
