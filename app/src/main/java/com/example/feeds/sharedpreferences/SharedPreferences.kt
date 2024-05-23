package com.example.feeds.sharedpreferences

import android.content.Context
import android.view.View
import com.example.feeds.OpeningScreen

class SharedPreferences() {
    fun saveLoginState(view: View, loggedIn: Boolean) {
        val sharedPreferences = view.context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("logged_in", loggedIn)
            apply()
        }
    }

    fun isUserLoggedInBefore(view: OpeningScreen) : Boolean {
        val sharedPreferences = view.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("logged_in", false)
    }
}