package com.steleot.sample

import android.app.Application
import com.steleot.blekotlin.BleClient
import com.steleot.blekotlin.BleConfig
import com.steleot.blekotlin.BuildConfig
import com.steleot.blekotlin.helper.BleLogger
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
                this,
                bleLogger = object : BleLogger {
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