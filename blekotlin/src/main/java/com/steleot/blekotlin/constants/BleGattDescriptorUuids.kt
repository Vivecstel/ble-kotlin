package com.steleot.blekotlin.constants

/**
 * The available uuids as Strings for the gatt descriptors. This list will updated constantly.
 * Based on [link](https://btprodspecificationrefs.blob.core.windows.net/assigned-values/16-bit%20UUID%20Numbers%20Document.pdf)
 */
object BleGattDescriptorUuids {

    const val CHARACTERISTIC_EXTENDED_PROPERTIES = "2900"
    const val CHARACTERISTIC_USER_DESCRIPTION = "2901"
    const val CLIENT_CHARACTERISTIC_CONFIGURATION = "2902"
    const val SERVER_CHARACTERISTIC_CONFIGURATION = "2903"
    const val CHARACTERISTIC_PRESENTATION_FORMAT = "2904"
    const val CHARACTERISTIC_AGGREGATE_FORMAT = "2905"
    const val VALID_RANGE = "2906"
    const val EXTERNAL_REPORT_REFERENCE = "2907"
    const val REPORT_REFERENCE = "2908"
    const val NUMBER_OF_DIGITALS = "2909"
    const val VALUE_TRIGGER_SETTING = "290A"
    const val ENVIRONMENTAL_SENSING_CONFIGURATION = "290B"
    const val ENVIRONMENTAL_SENSING_MEASUREMENT = "290C"
    const val ENVIRONMENTAL_SENSING_TRIGGER_SETTING = "290D"
    const val TIME_TRIGGER_SETTING = "290E"
    const val COMPLETE_BR_EDR_TRANSPORT_BLOCK_DATA = "290F"
}