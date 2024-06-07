package com.example.feeds

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.feeds.supabase.SupaBase

class AppLifecycleObserver(private val appContext: Context) : DefaultLifecycleObserver {
    private val supaBase = SupaBase()

    override fun onResume(owner: LifecycleOwner) {
        val userId = supaBase.getUser()?.id
        supaBase.updateUserStatus(userId, true, appContext)
        println("\n\nTHE APP IS RESUMED: $userId\n\n")

        try {
            supaBase.getUserStatus(userId, appContext)
        } catch (e: Exception) {
            println("Error tracking user status: $e")
        }
        super.onResume(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        val userId = supaBase.getUser()?.id
        userId?.let { supaBase.updateUserStatus(userId, false, appContext) }
        println("\n\nTHE APP IS STOPPED: $userId\n\n")
        super.onStop(owner)
    }
}
