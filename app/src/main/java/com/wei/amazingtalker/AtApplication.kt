package com.wei.amazingtalker

import android.app.Application
import com.wei.amazingtalker.core.common.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class AtApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}