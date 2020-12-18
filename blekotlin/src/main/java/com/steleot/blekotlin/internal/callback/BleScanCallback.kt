package com.steleot.blekotlin.internal.callback

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import com.steleot.blekotlin.BleLogger
import com.steleot.blekotlin.BleScanResult
import com.steleot.blekotlin.internal.UNKNOWN_ERROR
import com.steleot.blekotlin.internal.utils.scanCallbackStatuses

private const val TAG = "BleScanCallback"

internal class BleScanCallback(
    private val bleLogger: BleLogger,
    private val listener: BleScanCallbackListener
) : ScanCallback() {

    override fun onScanResult(
        callbackType: Int,
        result: ScanResult
    ) {
        listener.onScanResult(result)
    }

    override fun onBatchScanResults(
        results: MutableList<ScanResult>?
    ) {
        /* empty implementation */
    }

    override fun onScanFailed(
        errorCode: Int
    ) {
        bleLogger.log(
            TAG,
            "Error code $errorCode ${
                scanCallbackStatuses.getOrElse(errorCode) {
                    UNKNOWN_ERROR
                }
            }"
        )
        listener.onScanResult(null)
    }

    interface BleScanCallbackListener {
        fun onScanResult(scanResult: BleScanResult?)
    }
}