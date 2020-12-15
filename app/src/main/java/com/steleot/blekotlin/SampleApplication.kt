package com.steleot.blekotlin

import android.app.Application
import timber.log.Timber

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        BleClient.init(
            this,
            BleConfig(
                object : BleLogger {
                    override fun log(
                        tag: String,
                        message: String
                    ) {
                        Timber.tag(tag).d(message)
                    }
                }
            )
        )
    }
}