package com.example.trickgame

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * Created by thunder on 17-12-1.
 *
 */
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
    companion object {
         @SuppressLint("StaticFieldLeak")
         var context:Context? = null
            private set
    }
}