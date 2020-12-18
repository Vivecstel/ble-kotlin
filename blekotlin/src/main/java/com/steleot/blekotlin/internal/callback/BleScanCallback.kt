package com.steleot.blekotlin.internal.callback

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import com.steleot.blekotlin.BleClient
import com.steleot.blekotlin.BleLogger
import com.steleot.blekotlin.internal.UNKNOWN_ERROR
import com.steleot.blekotlin.internal.utils.scanCallbackStatuses

/**
 * Logger tag constant.
 */
private const val TAG = "BleScanCallback"

internal class BleScanCallback(
    private val logger: BleLogger
) : ScanCallback() {

    override fun onScanResult(
        callbackType: Int,
        result: ScanResult
    ) {
        BleClient.bleDevice.value = result to 0
    }

    override fun onBatchScanResults(
        results: MutableList<ScanResult>?
    ) {
        /* empty implementation */
    }

    override fun onScanFailed(
        errorCode: Int
    ) {
        logger.log(
            TAG,
            "Error code $errorCode ${scanCallbackStatuses.getOrElse(errorCode) { UNKNOWN_ERROR }}"
        )
        BleClient.bleDevice.value = null to errorCode
    }
}