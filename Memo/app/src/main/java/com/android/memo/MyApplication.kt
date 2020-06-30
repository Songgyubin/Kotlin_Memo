package com.android.memo

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)

    }

    companion object {
        private const val TAG = "MyApplication.kt"
    }
}