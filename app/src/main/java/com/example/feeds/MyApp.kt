package com.example.feeds

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(this))
    }
}
