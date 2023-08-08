package com.example.finalproject

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "MyAppPreferences",
        Context.MODE_PRIVATE
    )

    fun saveLastEmailAddress(email: String) {
        sharedPreferences.edit().putString("last_email", email).apply()
    }

    fun getLastEmailAddress(): String {
        return sharedPreferences.getString("last_email", "") ?: ""
    }
}