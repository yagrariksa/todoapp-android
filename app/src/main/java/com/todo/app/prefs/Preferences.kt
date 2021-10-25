package com.todo.app.prefs

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {

    private val PREFS_NAME = "com.yagrariksa.todo"
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val TOKEN = "TOKEN"
    private val USER_ID = "USER_ID"
    private val USER_NAME = "USER_NAME"

    var token: String?
        get() = prefs.getString(TOKEN, null)
        set(value) = prefs.edit().putString(TOKEN, value).apply()

    var userId: String?
        get() = prefs.getString(USER_ID, null)
        set(value) = prefs.edit().putString(USER_ID, value).apply()

    var userName: String?
        get() = prefs.getString(USER_NAME, null)
        set(value) = prefs.edit().putString(USER_NAME, value).apply()

    fun resetPreference() {
        prefs.edit().clear().apply()
    }
}