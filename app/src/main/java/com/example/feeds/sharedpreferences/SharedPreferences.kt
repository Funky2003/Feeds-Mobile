package com.example.feeds.sharedpreferences

import android.content.Context
import android.view.View

class SharedPreferences() {
    fun saveLoginState(view: View, loggedIn: Boolean) {
        val sharedPreferences = view.context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("logged_in", loggedIn)
            apply()
        }
    }

    fun isUserLoggedInBefore(view: View) : Boolean {
        val sharedPreferences = view.context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("logged_in", false)
    }
}