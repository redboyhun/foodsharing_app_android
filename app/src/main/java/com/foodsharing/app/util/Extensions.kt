package com.foodsharing.app.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.Response

fun View.visible() { visibility = View.VISIBLE }
fun View.gone() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun Fragment.toast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun <T> Response<T>.bodyOrThrow(): T {
    if (isSuccessful) return body() ?: throw Exception("Empty response body")
    throw Exception("API error ${code()}: ${errorBody()?.string()}")
}

fun <T> Response<T>.toResource(): Resource<T> {
    return try {
        if (isSuccessful) {
            val body = body()
            if (body != null) Resource.Success(body)
            else Resource.Error("Empty response")
        } else {
            Resource.Error("Error ${code()}: ${errorBody()?.string()}")
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Unknown error")
    }
}
