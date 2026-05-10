package com.foodsharing.app.util

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

fun httpErrorMessage(code: Int): String = when (code) {
    401 -> "Session expired — please log in again"
    403 -> "You don't have permission for this action"
    404 -> "Content not found"
    in 500..599 -> "Server error, please try again later"
    else -> "Network error ($code)"
}
