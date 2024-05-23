package com.example.feeds.sharedpreferences

import android.content.Context
import android.view.View
import com.example.feeds.DeepLinkHandlerActivity
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

    fun saveUserEmail(view: View, email: String) {
        val sharedPreferences = view.context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("email", email)
            apply()
        }
    }

    fun getUserEmail(view: View) : String? {
        val sharedPreferences = view.context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", "")
    }

    fun showSuccessGifState(context: Context, state: Boolean) {
        val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("account_verified", state)
            apply()
        }
    }

    fun isShowSuccessGifBefore(view: DeepLinkHandlerActivity) : Boolean {
        val sharedPreferences = view.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("account_verified", false)
    }
}