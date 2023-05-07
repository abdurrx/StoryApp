package com.dicoding.storyapp.data.source.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class UserPreferenceImpl @Inject constructor(val context: Context, private val sharedPreferences: SharedPreferences) {
    fun setToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(TOKEN, token)
            commit()
        }
    }

    fun getToken(): String {
        return sharedPreferences.getString(TOKEN, "") ?: ""
    }

    fun deleteToken() {
        with(sharedPreferences.edit()) {
            clear()
            commit()
        }
    }

    companion object{
        const val PREF_NAME = "PREF_NAME"
        const val TOKEN = "TOKEN"
    }
}