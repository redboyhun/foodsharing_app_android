package com.foodsharing.app.data.repository

import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.data.model.LoginRequest
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.SessionManager

class AuthRepository(private val sessionManager: SessionManager) {

    suspend fun login(email: String, password: String): Resource<Unit> {
        return try {
            val response = ApiClient.api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                // Check for X-CSRF-TOKEN header
                response.headers()["X-CSRF-TOKEN"]?.let { token ->
                    sessionManager.saveCsrfToken(token)
                }

                // Extract FS_CSRF_TOKEN from Set-Cookie header
                response.headers().values("Set-Cookie").find { it.contains("FS_CSRF_TOKEN=") }?.let { cookie ->
                    val token = cookie.substringAfter("FS_CSRF_TOKEN=").substringBefore(";")
                    sessionManager.saveCsrfToken(token)
                }
                response.headers().values("Set-Cookie").find { it.contains("FS_SESSID=") }?.let { cookie ->
                    val sessid = cookie.substringAfter("FS_SESSID=").substringBefore(";")
                    sessionManager.saveSession(sessid)
                }

                Resource.Success(Unit)
            } else {
                Resource.Error("Login failed: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun logout(): Resource<Unit> {
        return try {
            ApiClient.api.logout()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun getCurrentUser() = try {
        val response = ApiClient.api.getCurrentUser()
        if (response.isSuccessful && response.body() != null) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error("Failed to get user: ${response.code()}")
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Network error")
    }
}
