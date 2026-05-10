package com.foodsharing.app.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSession(sessionId: String) {
        prefs.edit().putString(KEY_SESSION_ID, sessionId).apply()
    }

    fun getSession(): String? = prefs.getString(KEY_SESSION_ID, null)

    fun saveUserId(userId: Int) {
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)

    fun saveUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }

    fun getUserName(): String? = prefs.getString(KEY_USER_NAME, null)

    fun saveUserAvatar(avatar: String?) {
        prefs.edit().putString(KEY_USER_AVATAR, avatar).apply()
    }

    fun getUserAvatar(): String? = prefs.getString(KEY_USER_AVATAR, null)

    fun saveCsrfToken(token: String) {
        prefs.edit().putString(KEY_CSRF_TOKEN, token).apply()
    }

    fun getCsrfToken(): String? = prefs.getString(KEY_CSRF_TOKEN, null)

    fun isLoggedIn(): Boolean = getSession() != null && getUserId() != -1

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        const val PREFS_NAME = "foodsharing_session"
        private const val KEY_SESSION_ID = "session_id"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_AVATAR = "user_avatar"
        private const val KEY_CSRF_TOKEN = "csrf_token"
    }
}
