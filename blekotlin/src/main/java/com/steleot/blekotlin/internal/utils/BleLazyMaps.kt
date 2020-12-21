package com.steleot.blekotlin.internal.utils

import com.steleot.blekotlin.BleAdapter
import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.BleGatt
import com.steleot.blekotlin.BleScanCallback
import com.steleot.blekotlin.constants.BleGattCharacteristics
import com.steleot.blekotlin.constants.BleGattDescriptors
import com.steleot.blekotlin.constants.BleGattServices

/**
 * The bluetooth statuses for logging purposes.
 */
internal val bleStatuses by lazy {
    mapOf(
        BleAdapter.STATE_OFF to "Bluetooth state off",
        BleAdapter.STATE_TURNING_ON to "Bluetooth state turning on",
        BleAdapter.STATE_ON to "Bluetooth state on",
        BleAdapter.STATE_TURNING_OFF to "Bluetooth state turning off",
    )
}

/**
 * The bluetooth device bond states for logging purposes.
 */
internal val bleBondStates by lazy {
    mapOf(
        BleDevice.BOND_NONE to "Bond none",
        BleDevice.BOND_BONDING to "Bond bonding",
        BleDevice.BOND_BONDED to "Bond bonded",
    )
}

/**
 * The bluetooth scan callback statuses for logging purposes.
 */
internal val scanCallbackStatuses by lazy {
    mapOf(
        BleScanCallback.SCAN_FAILED_ALREADY_STARTED
                to "Ble scan failed - Already started",
        BleScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED
                to "Ble scan failed - Application registration failed",
        BleScanCallback.SCAN_FAILED_INTERNAL_ERROR
                to "Ble scan failed - Internal server error",
        BleScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED
                to "Ble scan failed - Feature unsupported",
        5 to "Ble scan failed - out of hardware resources",
        6 to "Ble scan failed - scanning too frequently",
    )
}

/**
 * The bluetooth gatt statuses for logging purposes.
 */
internal val gattStatuses by lazy {
    mapOf(
        BleGatt.GATT_SUCCESS to "Gatt success",
        0x01 to "Gatt invalid handle",
        BleGatt.GATT_READ_NOT_PERMITTED to "Gatt read not permitted",
        BleGatt.GATT_WRITE_NOT_PERMITTED to "Gatt write not permitted",
        0x04 to "Gatt invalid pdu",
        BleGatt.GATT_INSUFFICIENT_AUTHENTICATION to "Gatt insufficient authentication",
        BleGatt.GATT_REQUEST_NOT_SUPPORTED to "Gatt request not supported",
        BleGatt.GATT_INVALID_OFFSET to "Gatt invalid offset",
        0x08 to "Gatt insufficient authorization or connection timeout",
        0x09 to "Gatt prepare q full",
        0x0a to "Gatt not found",
        0x0b to "Gatt not long",
        0x0c to "Gatt insufficient key size",
        BleGatt.GATT_INVALID_ATTRIBUTE_LENGTH to "Gatt invalid attribute length",
        0x0e to "Gatt error unlikely",
        BleGatt.GATT_INSUFFICIENT_ENCRYPTION to "Gatt insufficient encryption",
        0x10 to "Gatt unsupported grp type",
        0x11 to "Gatt insufficient resource",
        0x13 to "Gatt connection terminate peer user",
        0x16 to "Gatt connection terminate local host",
        0x22 to "Gatt connection lmp timeout",
        0x3e to "Gatt connection fail establish",
        0x87 to "GATT_ILLEGAL_PARAMETER",
        0x80 to "Gatt no resources",
        0x81 to "Gatt internal error",
        0x82 to "Gatt wrong state",
        0x83 to "Gatt db full",
        0x84 to "Gatt busy",
        0x85 to "Gatt error",
        0x86 to "Gatt cmd started",
        0x88 to "Gatt pending",
        0x89 to "Gatt auth fail",
        0x8a to "Gatt more",
        0x8b to "Gatt invalid cfg",
        0x8c to "Gatt service started",
        0x8d to "Gatt encrypted no mitm",
        0x8e to "Gatt not encrypted",
        BleGatt.GATT_CONNECTION_CONGESTED to "Gatt connection congested",
        0xfd to "Gatt ccc cfg err",
        0xfe to "Gatt prc in progress",
        0xff to "Gatt out of range",
        0x100 to "Gatt connection cancel",
        BleGatt.GATT_FAILURE to "Gatt failure"
    )
}

/**
 * The bluetooth gatt service uuids for logging purposes.
 */
internal val gattServicesUuids by lazy {
    mapOf(
        BleGattServices.ALERT_NOTIFICATION_SERVICE to "Alert Notification Service",
        BleGattServices.BATTERY_SERVICE to "Battery Service",
        BleGattServices.BLOOD_PRESSURE to "Blood Pressure",
        BleGattServices.BODY_COMPOSITION to "Body Composition",
        BleGattServices.BOND_MANAGEMENT to "Bond Management",
        BleGattServices.CONTINUOUS_GLUCOSE_MONITORING to "Continuous Glucose Monitoring",
        BleGattServices.CURRENT_TIME_SERVICE to "Current Time Service",
        BleGattServices.CYCLING_POWER to "Cycling Power",
        BleGattServices.CYCLING_SPEED_AND_CADENCE to "Cycling Speed and Cadence",
        BleGattServices.DEVICE_INFORMATION to "Device Information",
        BleGattServices.ENVIRONMENTAL_SENSING to "Environmental Sensing",
        BleGattServices.GENERIC_ACCESS to "Generic Access",
        BleGattServices.GENERIC_ATTRIBUTE to "Generic Attribute",
        BleGattServices.GLUCOSE to "Glucose",
        BleGattServices.HEALTH_THERMOMETER to "Health Thermometer",
        BleGattServices.HEART_RATE to "Heart Rate",
        BleGattServices.HUMAN_INTERFACE_DEVICE to "Human Interface Device",
        BleGattServices.IMMEDIATE_ALERT to "Immediate Alert",
        BleGattServices.LINK_LOSS to "Link Loss",
        BleGattServices.LOCATION_AND_NAVIGATION to "Location and Navigation",
        BleGattServices.INTERNET_PROTOCOL_SUPPORT to "Internet Protocol Support",
        BleGattServices.NEXT_DST_CHANGE_SERVICE to "Next DST Change Service",
        BleGattServices.PHONE_ALERT_STATUS_SERVICE to "Phone Alert Status Service",
        BleGattServices.REFERENCE_TIME_UPDATE_SERVICE to "Reference Time Update Service",
        BleGattServices.RUNNING_SPEED_AND_CADENCE to "Running Speed and Cadence",
        BleGattServices.SCAN_PARAMETERS to "Scan Parameters",
        BleGattServices.TX_POWER to "Tx Power",
        BleGattServices.USER_DATA to "User Data",
        BleGattServices.WEIGHT_SCALE to "Weight Scale",
        BleGattServices.AUTOMATION_IO to "Automation IO",
        BleGattServices.IMMEDIATE_ALERT_SERVICE_1_1 to "Immediate Alert Service 1.1",
        BleGattServices.LINK_LOSS_SERVICE_1_1 to "Link Loss Service 1.1",
        BleGattServices.TX_POWER_SERVICE_1_1 to "Tx Power Service 1.1",
    )
}

/**
 * The bluetooth gatt characteristic uuids for logging purposes.
 */
internal val gattCharacteristicUuids by lazy {
    mapOf(
        BleGattCharacteristics.AEROBIC_HEART_RATE_LOWER_LIMIT to "Aerobic Heart Rate Lower Limit",
        BleGattCharacteristics.AEROBIC_HEART_RATE_UPPER_LIMIT to "Aerobic Heart Rate Upper Limit",
        BleGattCharacteristics.AEROBIC_THRESHOLD to "Aerobic Threshold",
        BleGattCharacteristics.AGE to "Age",
        BleGattCharacteristics.ALERT_CATEGORY_ID to "Alert Category ID",
        BleGattCharacteristics.ALERT_CATEGORY_ID_BIT_MASK to "Alert Category ID Bit Mask",
        BleGattCharacteristics.ALERT_LEVEL to "Alert Level",
        BleGattCharacteristics.ALERT_NOTIFICATION_CONTROL_POINT to "Alert Notification Control Point",
        BleGattCharacteristics.ALERT_STATUS to "Alert Status",
        BleGattCharacteristics.ANAEROBIC_HEART_RATE_LOWER_LIMIT to "Anaerobic Heart Rate Lower Limit",
        BleGattCharacteristics.ANAEROBIC_HEART_RATE_UPPER_LIMIT to "Anaerobic Heart Rate Upper Limit",
        BleGattCharacteristics.ANAEROBIC_THRESHOLD to "Anaerobic Threshold",
        BleGattCharacteristics.APPARENT_WIND_DIRECTION to "Apparent Wind Direction",
        BleGattCharacteristics.APPARENT_WIND_SPEED to "Apparent Wind Speed",
        BleGattCharacteristics.APPEARANCE to "Appearance",
        BleGattCharacteristics.BAROMETRIC_PRESSURE_TREND to "Barometric Pressure Trend",
        BleGattCharacteristics.BATTERY_LEVEL to "Battery Level",
        BleGattCharacteristics.BLOOD_PRESSURE_FEATURE to "Blood Pressure Feature",
        BleGattCharacteristics.BLOOD_PRESSURE_MEASUREMENT to "Blood Pressure Measurement",
        BleGattCharacteristics.BODY_COMPOSITION_FEATURE to "Body Composition Feature",
        BleGattCharacteristics.BODY_COMPOSITION_MEASUREMENT to "Body Composition Measurement",
        BleGattCharacteristics.BODY_SENSOR_LOCATION to "Body Sensor Location",
        BleGattCharacteristics.BOND_MANAGEMENT_CONTROL_POINT to "Bond Management Control Point",
        BleGattCharacteristics.BOND_MANAGEMENT_FEATURE to "Bond Management Feature",
        BleGattCharacteristics.BOOT_KEYBOARD_INPUT_REPORT to "Boot Keyboard Input Report",
        BleGattCharacteristics.BOOT_KEYBOARD_OUTPUT_REPORT to "Boot Keyboard Output Report",
        BleGattCharacteristics.BOOT_MOUSE_INPUT_REPORT to "Boot Mouse Input Report",
        BleGattCharacteristics.CENTRAL_ADDRESS_RESOLUTION to "Central Address Resolution",
        BleGattCharacteristics.CGM_FEATURE to "CGM Feature",
        BleGattCharacteristics.CGM_MEASUREMENT to "CGM Measurement",
        BleGattCharacteristics.CGM_SESSION_RUN_TIME to "CGM Session Run Time",
        BleGattCharacteristics.CGM_SESSION_START_TIME to "CGM Session Start Time",
        BleGattCharacteristics.CGM_SPECIFIC_OPS_CONTROL_POINT to "CGM Specific Ops Control Point",
        BleGattCharacteristics.CGM_STATUS to "CGM Status",
        BleGattCharacteristics.CSC_FEATURE to "CSC Feature",
        BleGattCharacteristics.CSC_MEASUREMENT to "CSC Measurement",
        BleGattCharacteristics.CURRENT_TIME to "Current Time",
        BleGattCharacteristics.CYCLING_POWER_CONTROL_POINT to "Cycling Power Control Point",
        BleGattCharacteristics.CYCLING_POWER_FEATURE to "Cycling Power Feature",
        BleGattCharacteristics.CYCLING_POWER_MEASUREMENT to "Cycling Power Measurement",
        BleGattCharacteristics.CYCLING_POWER_VECTOR to "Cycling Power Vector",
        BleGattCharacteristics.DATABASE_CHANGE_INCREMENT to "Database Change Increment",
        BleGattCharacteristics.DATE_OF_BIRTH to "Date of Birth",
        BleGattCharacteristics.DATE_OF_THRESHOLD_ASSESSMENT to "Date of Threshold Assessment",
        BleGattCharacteristics.DATE_TIME to "Date Time",
        BleGattCharacteristics.DAY_DATE_TIME to "Day Date Time",
        BleGattCharacteristics.DAY_OF_WEEK to "Day of Week",
        BleGattCharacteristics.DESCRIPTOR_VALUE_CHANGED to "Descriptor Value Changed",
        BleGattCharacteristics.DEVICE_NAME to "Device Name",
        BleGattCharacteristics.DEW_POINT to "Dew Point",
        BleGattCharacteristics.DST_OFFSET to "DST Offset",
        BleGattCharacteristics.ELEVATION to "Elevation",
        BleGattCharacteristics.EMAIL_ADDRESS to "Email Address",
        BleGattCharacteristics.EXACT_TIME_256 to "Exact Time 256",
        BleGattCharacteristics.FAT_BURN_HEART_RATE_LOWER_LIMIT to "Fat Burn Heart Rate Lower Limit",
        BleGattCharacteristics.FAT_BURN_HEART_RATE_UPPER_LIMIT to "Fat Burn Heart Rate Upper Limit",
        BleGattCharacteristics.FIRMWARE_REVISION_STRING to "Firmware Revision String",
        BleGattCharacteristics.FIRST_NAME to "First Name",
        BleGattCharacteristics.FIVE_ZONE_HEART_RATE_LIMITS to "Five Zone Heart Rate Limits",
        BleGattCharacteristics.GENDER to "Gender",
        BleGattCharacteristics.GLUCOSE_FEATURE to "Glucose Feature",
        BleGattCharacteristics.GLUCOSE_MEASUREMENT to "Glucose Measurement",
        BleGattCharacteristics.GLUCOSE_MEASUREMENT_CONTEXT to "Glucose Measurement Context",
        BleGattCharacteristics.GUST_FACTOR to "Gust Factor",
        BleGattCharacteristics.HARDWARE_REVISION_STRING to "Hardware Revision String",
        BleGattCharacteristics.HEART_RATE_CONTROL_POINT to "Heart Rate Control Point",
        BleGattCharacteristics.HEART_RATE_MAX to "Heart Rate Max",
        BleGattCharacteristics.HEART_RATE_MEASUREMENT to "Heart Rate Measurement",
        BleGattCharacteristics.HEAT_INDEX to "Heat Index",
        BleGattCharacteristics.HEIGHT to "Height",
        BleGattCharacteristics.HID_CONTROL_POINT to "HID Control Point",
        BleGattCharacteristics.HID_INFORMATION to "HID Information",
        BleGattCharacteristics.HIP_CIRCUMFERENCE to "Hip Circumference",
        BleGattCharacteristics.HUMIDITY to "Humidity",
        BleGattCharacteristics.IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST to "IEEE 11073-20601 Regulatory Certification Data List",
        BleGattCharacteristics.INTERMEDIATE_CUFF_PRESSURE to "Intermediate Cuff Pressure",
        BleGattCharacteristics.INTERMEDIATE_TEMPERATURE to "Intermediate Temperature",
        BleGattCharacteristics.IRRADIANCE to "Irradiance",
        BleGattCharacteristics.LANGUAGE to "Language",
        BleGattCharacteristics.LAST_NAME to "Last Name",
        BleGattCharacteristics.LN_CONTROL_POINT to "LN Control Point",
        BleGattCharacteristics.LN_FEATURE to "LN Feature",
        BleGattCharacteristics.LOCAL_TIME_INFORMATION to "Local Time Information",
        BleGattCharacteristics.LOCATION_AND_SPEED to "Location and Speed",
        BleGattCharacteristics.MAGNETIC_DECLINATION to "Magnetic Declination",
        BleGattCharacteristics.MAGNETIC_FLUX_DENSITY_2D to "Magnetic Flux Density - 2D",
        BleGattCharacteristics.MAGNETIC_FLUX_DENSITY_3D to "Magnetic Flux Density - 3D",
        BleGattCharacteristics.MANUFACTURER_NAME_STRING to "Manufacturer Name String",
        BleGattCharacteristics.MAXIMUM_RECOMMENDED_HEART_RATE to "Maximum Recommended Heart Rate",
        BleGattCharacteristics.MEASUREMENT_INTERVAL to "Measurement Interval",
        BleGattCharacteristics.MODEL_NUMBER_STRING to "Model Number String",
        BleGattCharacteristics.NAVIGATION to "Navigation",
        BleGattCharacteristics.NEW_ALERT to "New Alert",
        BleGattCharacteristics.PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS to "Peripheral Preferred Connection Parameters",
        BleGattCharacteristics.PERIPHERAL_PRIVACY_FLAG to "Peripheral Privacy Flag",
        BleGattCharacteristics.PNP_ID to "PnP ID",
        BleGattCharacteristics.POLLEN_CONCENTRATION to "Pollen Concentration",
        BleGattCharacteristics.POSITION_QUALITY to "Position Quality",
        BleGattCharacteristics.PRESSURE to "Pressure",
        BleGattCharacteristics.PROTOCOL_MODE to "Protocol Mode",
        BleGattCharacteristics.RAINFALL to "Rainfall",
        BleGattCharacteristics.RECONNECTION_ADDRESS to "Reconnection Address",
        BleGattCharacteristics.RECORD_ACCESS_CONTROL_POINT to "Record Access Control Point",
        BleGattCharacteristics.REFERENCE_TIME_INFORMATION to "Reference Time Information",
        BleGattCharacteristics.REPORT to "Report",
        BleGattCharacteristics.REPORT_MAP to "Report Map",
        BleGattCharacteristics.RESTING_HEART_RATE to "Resting Heart Rate",
        BleGattCharacteristics.RINGER_CONTROL_POINT to "Ringer Control Point",
        BleGattCharacteristics.RINGER_SETTING to "Ringer Setting",
        BleGattCharacteristics.RSC_FEATURE to "RSC Feature",
        BleGattCharacteristics.RSC_MEASUREMENT to "RSC Measurement",
        BleGattCharacteristics.SC_CONTROL_POINT to "SC Control Point",
        BleGattCharacteristics.SCAN_INTERVAL_WINDOW to "Scan Interval Window",
        BleGattCharacteristics.SCAN_REFRESH to "Scan Refresh",
        BleGattCharacteristics.SENSOR_LOCATION to "Sensor Location",
        BleGattCharacteristics.SERIAL_NUMBER_STRING to "Serial Number String",
        BleGattCharacteristics.SERVICE_CHANGED to "Service Changed",
        BleGattCharacteristics.SOFTWARE_REVISION_STRING to "Software Revision String",
        BleGattCharacteristics.SPORT_TYPE_FOR_AEROBIC_AND_ANAEROBIC_THRESHOLDS to "Sport Type for Aerobic and Anaerobic Thresholds",
        BleGattCharacteristics.SUPPORTED_NEW_ALERT_CATEGORY to "Supported New Alert Category",
        BleGattCharacteristics.SUPPORTED_UNREAD_ALERT_CATEGORY to "Supported Unread Alert Category",
        BleGattCharacteristics.SYSTEM_ID to "System ID",
        BleGattCharacteristics.TEMPERATURE to "Temperature",
        BleGattCharacteristics.TEMPERATURE_MEASUREMENT to "Temperature Measurement",
        BleGattCharacteristics.TEMPERATURE_TYPE to "Temperature Type",
        BleGattCharacteristics.THREE_ZONE_HEART_RATE_LIMITS to "Three Zone Heart Rate Limits",
        BleGattCharacteristics.TIME_ACCURACY to "Time Accuracy",
        BleGattCharacteristics.TIME_SOURCE to "Time Source",
        BleGattCharacteristics.TIME_UPDATE_CONTROL_POINT to "Time Update Control Point",
        BleGattCharacteristics.TIME_UPDATE_STATE to "Time Update State",
        BleGattCharacteristics.TIME_WITH_DST to "Time with DST",
        BleGattCharacteristics.TIME_ZONE to "Time Zone",
        BleGattCharacteristics.TRUE_WIND_DIRECTION to "True Wind Direction",
        BleGattCharacteristics.TRUE_WIND_SPEED to "True Wind Speed",
        BleGattCharacteristics.TWO_ZONE_HEART_RATE_LIMIT to "Two Zone Heart Rate Limit",
        BleGattCharacteristics.TX_POWER_LEVEL to "Tx Power Level",
        BleGattCharacteristics.UNREAD_ALERT_STATUS to "Unread Alert Status",
        BleGattCharacteristics.USER_CONTROL_POINT to "User Control Point",
        BleGattCharacteristics.USER_INDEX to "User Index",
        BleGattCharacteristics.UV_INDEX to "UV Index",
        BleGattCharacteristics.VO2_MAX to "VO2 Max",
        BleGattCharacteristics.WAIST_CIRCUMFERENCE to "Waist Circumference",
        BleGattCharacteristics.WEIGHT to "Weight",
        BleGattCharacteristics.WEIGHT_MEASUREMENT to "Weight Measurement",
        BleGattCharacteristics.WEIGHT_SCALE_FEATURE to "Weight Scale Feature",
        BleGattCharacteristics.WIND_CHILL to "Wind Chill",
        BleGattCharacteristics.AGGREGATE to "Aggregate",
        BleGattCharacteristics.ALTITUDE to "Altitude",
        BleGattCharacteristics.ANALOG to "Analog",
        BleGattCharacteristics.DIGITAL to "Digital",
    )
}

/**
 * The bluetooth gatt descriptor uuids for logging purposes.
 */
internal val gattDescriptorUuids by lazy {
    mapOf(
        BleGattDescriptors.CHARACTERISTIC_EXTENDED_PROPERTIES to "Characteristic Extended Properties",
        BleGattDescriptors.CHARACTERISTIC_USER_DESCRIPTION to "Characteristic User Description",
        BleGattDescriptors.CLIENT_CHARACTERISTIC_CONFIGURATION to "Client Characteristic Configuration",
        BleGattDescriptors.SERVER_CHARACTERISTIC_CONFIGURATION to "Server Characteristic Configuration",
        BleGattDescriptors.CHARACTERISTIC_PRESENTATION_FORMAT to "Characteristic Presentation Format",
        BleGattDescriptors.CHARACTERISTIC_AGGREGATE_FORMAT to "Characteristic Aggregate Format",
        BleGattDescriptors.VALID_RANGE to "Valid Range",
        BleGattDescriptors.EXTERNAL_REPORT_REFERENCE to "External Report Reference",
        BleGattDescriptors.REPORT_REFERENCE to "Report Reference",
        BleGattDescriptors.ENVIRONMENTAL_SENSING_CONFIGURATION to "Environmental Sensing Configuration",
        BleGattDescriptors.ENVIRONMENTAL_SENSING_MEASUREMENT to "Environmental Sensing Measurement",
        BleGattDescriptors.ENVIRONMENTAL_SENSING_TRIGGER_SETTING to "Environmental Sensing Trigger Setting",
        BleGattDescriptors.NUMBER_OF_DIGITALS to "Number of Digitals",
        BleGattDescriptors.VALUE_TRIGGER_SETTING to "Value Trigger Setting",
        BleGattDescriptors.TIME_TRIGGER_SETTING to "Time Trigger Setting",
    )
}
