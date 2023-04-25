package com.wei.amazingtalker_recruit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AtApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}