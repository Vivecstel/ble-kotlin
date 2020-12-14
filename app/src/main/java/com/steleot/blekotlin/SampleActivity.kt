package com.steleot.blekotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        GlobalScope.launch {
            BleKotlin.status.collect { status ->
                when (status) {
                    is BleStatus.NotStarted -> Timber.d("Not started")
                    is BleStatus.BluetoothNotAvailable -> Timber.d("Bluetooth not available")
                    is BleStatus.LocationPermissionNotGranted -> Timber.d("Location Permission not granted")
                    is BleStatus.BluetoothNotEnabled -> Timber.d("Bluetooth not enabled")
                    is BleStatus.LocationServicesNotEnabled -> Timber.d("Location services not enabled")
                    is BleStatus.Ready -> Timber.d("ready")
                    else -> Timber.d("else")
                }
            }
        }
        BleKotlin.init(this)
        BleKotlin.startBleScan()
    }
}