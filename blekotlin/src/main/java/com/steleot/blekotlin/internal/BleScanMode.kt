package com.steleot.blekotlin.internal

/**
 * Ble Scan mode sealed class.
 */
internal sealed class BleScanMode {

    /**
     * Ble Scan mode - None.
     */
    object NoneMode : BleScanMode()

    /**
     * Ble Scan mode - Single. Meaning it will return one scan result.
     */
    object SingleMode : BleScanMode()

    /**
     * Ble Scan mode - Multiple. Meaning it will return list of scan results (unique per address).
     */
    object ListMode : BleScanMode()
}
