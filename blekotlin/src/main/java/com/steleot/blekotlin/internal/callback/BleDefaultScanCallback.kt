package com.steleot.blekotlin.internal.callback

import com.steleot.blekotlin.BleScanCallback
import com.steleot.blekotlin.BleScanResult
import com.steleot.blekotlin.helper.BleLogger
import com.steleot.blekotlin.internal.callback.BleDefaultScanCallback.BleScanCallbackListener
import com.steleot.blekotlin.internal.utils.getBleScanCallbackStatus

private const val TAG = "BleScanCallback"

/**
 * Internal class implementing the [BleScanCallback].
 * @param bleLogger: the [BleLogger] instance.
 * @param listener: the [BleScanCallbackListener] instance for communicating with the BleClient.
 */
internal class BleDefaultScanCallback(
    private val bleLogger: BleLogger,
    private val listener: BleScanCallbackListener
) : BleScanCallback() {

    override fun onScanResult(
        callbackType: Int,
        result: BleScanResult
    ) {
        listener.onScanResult(result)
    }

    override fun onBatchScanResults(
        results: List<BleScanResult>?
    ) {
        listener.onBatchScanResult(results)
    }

    override fun onScanFailed(
        errorCode: Int
    ) {
        bleLogger.log(
            TAG,
            "Error code ${errorCode.getBleScanCallbackStatus()}"
        )
        listener.onScanResult(null)
    }

    /**
     * The listener interface that connect the [BleDefaultScanCallback] with the
     * [com.steleot.blekotlin.BleClient].
     */
    interface BleScanCallbackListener {

        /**
         * Returns the blescan result if successful.
         * @param bleScanResult: [BleScanResult] if successful, null otherwise.
         */
        fun onScanResult(bleScanResult: BleScanResult?)

        /**
         * Returns a list of blescan result if successful.
         * @param bleScanResults: [List] of [BleScanResult] if successful, null otherwise.
         */
        fun onBatchScanResult(bleScanResults: List<BleScanResult>?)
    }
}