package com.foodsharing.app.data.api

import com.foodsharing.app.util.AuthEventBus
import com.foodsharing.app.util.SessionManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private var _api: FoodsharingApi? = null
    private var _baseUrl: String = "https://foodsharing.de/"
    private var _sessionManager: SessionManager? = null

    val api: FoodsharingApi
        get() = _api ?: throw IllegalStateException("ApiClient not initialized")

    val baseUrl: String
        get() = _baseUrl

    fun initialize(baseUrl: String, sessionManager: SessionManager) {
        _baseUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        _sessionManager = sessionManager
        _api = buildApi(sessionManager)
    }

    private fun buildApi(sessionManager: SessionManager): FoodsharingApi {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val cookieJar = object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookies.forEach { cookie ->
                    if (cookie.name == "PHPSESSID") {
                        sessionManager.saveSession(cookie.value)
                    }
                }
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                // We handle session cookies in the interceptor to include FS_CSRF_TOKEN and FS_SESSID
                return emptyList()
            }
        }

        val client = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor { chain ->
                val original = chain.request()
                // Only exclude the initial login request from needing a CSRF token
                val isLogin = original.url.encodedPath.endsWith("api/user/login")

                val builder = original.newBuilder()
                    .header("accept", "*/*")

                if (!isLogin) {
                    val csrfToken = sessionManager.getCsrfToken()
                    val sessionId = sessionManager.getSession()

                    if (csrfToken != null) {
                        builder.header("X-CSRF-Token", csrfToken)
                    }

                    val cookies = mutableListOf<String>()
                    if (csrfToken != null) {
                        cookies.add("FS_CSRF_TOKEN=$csrfToken")
                    }
                    if (sessionId != null) {
                        cookies.add("FS_SESSID=$sessionId")
                    }

                    if (cookies.isNotEmpty()) {
                        builder.header("Cookie", cookies.joinToString("; "))
                    }
                }

                val response = chain.proceed(builder.build())

                if (response.code == 403) {
                    sessionManager.clearSession()
                    AuthEventBus.emitSessionExpired()
                }

                response
            }
            .addInterceptor(loggingInterceptor) // Log after all headers are added
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(_baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(FoodsharingApi::class.java)
    }

    fun reinitialize(baseUrl: String) {
        _sessionManager?.let { initialize(baseUrl, it) }
    }
}
