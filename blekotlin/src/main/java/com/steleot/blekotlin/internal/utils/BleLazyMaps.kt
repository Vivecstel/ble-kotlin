package com.steleot.blekotlin.internal.utils

import com.steleot.blekotlin.BleAdapter
import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.BleGatt
import com.steleot.blekotlin.BleScanCallback
import com.steleot.blekotlin.constants.BleGattCharacteristicUuids
import com.steleot.blekotlin.constants.BleGattDescriptorUuids
import com.steleot.blekotlin.constants.BleGattServiceUuids

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
        BleGattServiceUuids.GENERIC_ACCESS to "Generic Access",
        BleGattServiceUuids.GENERIC_ATTRIBUTE to "Generic Attribute",
        BleGattServiceUuids.IMMEDIATE_ALERT to "Immediate Alert",
        BleGattServiceUuids.LINK_LOSS to "Link Loss",
        BleGattServiceUuids.TX_POWER to "Tx Power",
        BleGattServiceUuids.CURRENT_TIME_SERVICE to "Current Time Service",
        BleGattServiceUuids.REFERENCE_TIME_UPDATE_SERVICE to "Reference Time Update Service",
        BleGattServiceUuids.NEXT_DST_CHANGE_SERVICE to "Next DST Change Service",
        BleGattServiceUuids.GLUCOSE to "Glucose",
        BleGattServiceUuids.HEALTH_THERMOMETER to "Health Thermometer",
        BleGattServiceUuids.DEVICE_INFORMATION to "Device Information",
        BleGattServiceUuids.HEART_RATE to "Heart Rate",
        BleGattServiceUuids.PHONE_ALERT_STATUS_SERVICE to "Phone Alert Status Service",
        BleGattServiceUuids.BATTERY_SERVICE to "Battery Service",
        BleGattServiceUuids.BLOOD_PRESSURE to "Blood Pressure",
        BleGattServiceUuids.ALERT_NOTIFICATION_SERVICE to "Alert Notification Service",
        BleGattServiceUuids.HUMAN_INTERFACE_DEVICE to "Human Interface Device",
        BleGattServiceUuids.SCAN_PARAMETERS to "Scan Parameters",
        BleGattServiceUuids.RUNNING_SPEED_AND_CADENCE to "Running Speed and Cadence",
        BleGattServiceUuids.AUTOMATION_IO to "Automation IO",
        BleGattServiceUuids.CYCLING_SPEED_AND_CADENCE to "Cycling Speed and Cadence",
        BleGattServiceUuids.CYCLING_POWER to "Cycling Power",
        BleGattServiceUuids.LOCATION_AND_NAVIGATION to "Location and Navigation",
        BleGattServiceUuids.ENVIRONMENTAL_SENSING to "Environmental Sensing",
        BleGattServiceUuids.BODY_COMPOSITION to "Body Composition",
        BleGattServiceUuids.USER_DATA to "User Data",
        BleGattServiceUuids.WEIGHT_SCALE to "Weight Scale",
        BleGattServiceUuids.BOND_MANAGEMENT to "Bond Management",
        BleGattServiceUuids.CONTINUOUS_GLUCOSE_MONITORING to "Continuous Glucose Monitoring",
        BleGattServiceUuids.INTERNET_PROTOCOL_SUPPORT to "Internet Protocol Support",
        BleGattServiceUuids.INDOOR_PROTOCOL_SUPPORT to "Indoor Protocol Support",
        BleGattServiceUuids.PULSE_OXIMETER to "Pulse Oximeter",
        BleGattServiceUuids.HTTP_PROXY to "Http Proxy",
        BleGattServiceUuids.TRANSPORT_DISCOVERY to "Transport Discovery",
        BleGattServiceUuids.OBJECT_TRANSFER to "Object Transfer",
        BleGattServiceUuids.FITNESS_MACHINE to "Fitness Machine",
        BleGattServiceUuids.MESH_PROVISIONING to "Mesh Provisioning",
        BleGattServiceUuids.MESH_PROXY to "Mesh Proxy",
        BleGattServiceUuids.RECONNECTION_CONFIGURATION to "Reconnection Configuration",
        BleGattServiceUuids.INSULIN_DELIVERY to "Insulin Delivery",
        BleGattServiceUuids.BINARY_SENSOR to "Binary Sensor",
        BleGattServiceUuids.EMERGENCY_CONFIGURATION to "Emergency Configuration",
        BleGattServiceUuids.PHYSICAL_ACTIVITY_MONITOR to "",
        BleGattServiceUuids.AUDIO_INPUT_CONTROL to "Audio Input Control",
        BleGattServiceUuids.VOLUME_CONTROL to "Volume Control",
        BleGattServiceUuids.VOLUME_OFFSET_CONTROL to "Volume Offset Control",
        BleGattServiceUuids.DEVICE_TIME to "Device Time",
    )
}

/**
 * The bluetooth gatt characteristic uuids for logging purposes.
 */
internal val gattCharacteristicUuids by lazy {
    mapOf(
        BleGattCharacteristicUuids.DEVICE_NAME to "Device Name",
        BleGattCharacteristicUuids.APPEARANCE to "Appearance",
        BleGattCharacteristicUuids.PERIPHERAL_PRIVACY_FLAG to "Peripheral Privacy Flag",
        BleGattCharacteristicUuids.RECONNECTION_ADDRESS to "Reconnection Address",
        BleGattCharacteristicUuids.PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS to "Peripheral Preferred Connection Parameters",
        BleGattCharacteristicUuids.SERVICE_CHANGED to "Service Changed",
        BleGattCharacteristicUuids.ALERT_LEVEL to "Alert Level",
        BleGattCharacteristicUuids.TX_POWER_LEVEL to "Tx Power Level",
        BleGattCharacteristicUuids.DATE_TIME to "Date Time",
        BleGattCharacteristicUuids.DAY_OF_WEEK to "Day Of Week",
        BleGattCharacteristicUuids.DAY_DATE_TIME to "Day Date Time",
        BleGattCharacteristicUuids.EXACT_TIME_256 to "Exact Time256",
        BleGattCharacteristicUuids.DST_OFFSET to "Dst Offset",
        BleGattCharacteristicUuids.TIME_ZONE to "Time Zone",
        BleGattCharacteristicUuids.LOCAL_TIME_INFORMATION to "Local Time Information",
        BleGattCharacteristicUuids.TIME_WITH_DST to "Time With Dst",
        BleGattCharacteristicUuids.TIME_ACCURACY to "Time Accuracy",
        BleGattCharacteristicUuids.TIME_SOURCE to "Time Source",
        BleGattCharacteristicUuids.REFERENCE_TIME_INFORMATION to "Reference Time Information",
        BleGattCharacteristicUuids.TIME_UPDATE_CONTROL_POINT to "Time Update Control Point",
        BleGattCharacteristicUuids.TIME_UPDATE_STATE to "Time Update State",
        BleGattCharacteristicUuids.GLUCOSE_MEASUREMENT to "Glucose Measurement",
        BleGattCharacteristicUuids.BATTERY_LEVEL to "Battery Level",
        BleGattCharacteristicUuids.TEMPERATURE_MEASUREMENT to "Temperature Measurement",
        BleGattCharacteristicUuids.TEMPERATURE_TYPE to "Temperature Type",
        BleGattCharacteristicUuids.INTERMEDIATE_TEMPERATURE to "Intermediate Temperature",
        BleGattCharacteristicUuids.MEASUREMENT_INTERVAL to "Measurement Interval",
        BleGattCharacteristicUuids.BOOT_KEYBOARD_INPUT_REPORT to "Boot Keyobard Input Report",
        BleGattCharacteristicUuids.SYSTEM_ID to "System Id",
        BleGattCharacteristicUuids.MODEL_NUMBER_STRING to "Model Number String",
        BleGattCharacteristicUuids.SERIAL_NUMBER_STRING to "Serial Number String",
        BleGattCharacteristicUuids.FIRMWARE_REVISION_STRING to "Firmware Revision String",
        BleGattCharacteristicUuids.HARDWARE_REVISION_STRING to "Hardware Revision String",
        BleGattCharacteristicUuids.SOFTWARE_REVISION_STRING to "Software Revision String",
        BleGattCharacteristicUuids.MANUFACTURER_NAME_STRING to "Manufacturer Name String",
        BleGattCharacteristicUuids.IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST to "Iee 11073 20601 Regulatory Certification Data List",
        BleGattCharacteristicUuids.CURRENT_TIME to "Current Time",
        BleGattCharacteristicUuids.SCAN_REFRESH to "Scan Refresh",
        BleGattCharacteristicUuids.BOOT_KEYBOARD_OUTPUT_REPORT to "Boot Keyboard Ouput Report",
        BleGattCharacteristicUuids.BOOT_MOUSE_INPUT_REPORT to "Boot Mouse Input Report",
        BleGattCharacteristicUuids.GLUCOSE_MEASUREMENT_CONTEXT to "Glucose Measurement Context",
        BleGattCharacteristicUuids.BLOOD_PRESSURE_MEASUREMENT to "Blood Pressure Measurement",
        BleGattCharacteristicUuids.INTERMEDIATE_CUFF_PRESSURE to "Intermediate Cuff Pressure",
        BleGattCharacteristicUuids.HEART_RATE_MEASUREMENT to "Heart Rate Measurement",
        BleGattCharacteristicUuids.BODY_SENSOR_LOCATION to "Body Sensor Location",
        BleGattCharacteristicUuids.HEART_RATE_CONTROL_POINT to "Heart Rate Control Point",
        BleGattCharacteristicUuids.ALERT_STATUS to "Alert Status",
        BleGattCharacteristicUuids.RINGER_CONTROL_POINT to "Ringer Control Point",
        BleGattCharacteristicUuids.RINGER_SETTING to "Ringer Setting",
        BleGattCharacteristicUuids.ALERT_CATEGORY_ID_BIT_MASK to "Alert Category Id Bit Mask",
        BleGattCharacteristicUuids.ALERT_CATEGORY_ID to "Alert Category Id",
        BleGattCharacteristicUuids.ALERT_NOTIFICATION_CONTROL_POINT to "Alert Notification Control Point",
        BleGattCharacteristicUuids.UNREAD_ALERT_STATUS to "Unread Alert Status",
        BleGattCharacteristicUuids.NEW_ALERT to "New Alert",
        BleGattCharacteristicUuids.SUPPORTED_NEW_ALERT_CATEGORY to "Supported New Alert Category",
        BleGattCharacteristicUuids.SUPPORTED_UNREAD_ALERT_CATEGORY to "Supported Unread Alert Category",
        BleGattCharacteristicUuids.BLOOD_PRESSURE_FEATURE to "Blood Pressure Feature",
        BleGattCharacteristicUuids.HID_INFORMATION to "Hid Information",
        BleGattCharacteristicUuids.REPORT_MAP to "Report Map",
        BleGattCharacteristicUuids.HID_CONTROL_POINT to "Hid Control Point",
        BleGattCharacteristicUuids.REPORT to "Report",
        BleGattCharacteristicUuids.PROTOCOL_MODE to "Protocol Mode",
        BleGattCharacteristicUuids.SCAN_INTERVAL_WINDOW to "Scan Interval Window",
        BleGattCharacteristicUuids.PNP_ID to "Pnp Id",
        BleGattCharacteristicUuids.GLUCOSE_FEATURE to "Glucose Feature",
        BleGattCharacteristicUuids.RECORD_ACCESS_CONTROL_POINT to "Record Access Control Point",
        BleGattCharacteristicUuids.RSC_MEASUREMENT to "Rsc Measurement",
        BleGattCharacteristicUuids.RSC_FEATURE to "Rsc Feature",
        BleGattCharacteristicUuids.SC_CONTROL_POINT to "Sc Control Point",
        BleGattCharacteristicUuids.AGGREGATE to "Aggregate",
        BleGattCharacteristicUuids.CSC_MEASUREMENT to "Csc Measurement",
        BleGattCharacteristicUuids.CSC_FEATURE to "Csc Feature",
        BleGattCharacteristicUuids.SENSOR_LOCATION to "Sensor Location",
        BleGattCharacteristicUuids.PLX_SPOT_CHECK_MEASUREMENT to "Plx Spot Check Measurement",
        BleGattCharacteristicUuids.PLX_CONTINUOUS_MEASUREMENT to "Plx Continuous Measurement",
        BleGattCharacteristicUuids.PLX_FEATURES to "Plx Features",
        BleGattCharacteristicUuids.CYCLING_POWER_MEASUREMENT to "Cycling Power Measurement",
        BleGattCharacteristicUuids.CYCLING_POWER_VECTOR to "Cycling Power Vector",
        BleGattCharacteristicUuids.CYCLING_POWER_FEATURE to "Cycling Power Feature",
        BleGattCharacteristicUuids.CYCLING_POWER_CONTROL_POINT to "Cycling Power Control Point",
        BleGattCharacteristicUuids.LOCATION_AND_SPEED to "Location And Speed",
        BleGattCharacteristicUuids.NAVIGATION to "Navigation",
        BleGattCharacteristicUuids.POSITION_QUALITY to "Position Quality",
        BleGattCharacteristicUuids.LN_FEATURE to "Ln Feature",
        BleGattCharacteristicUuids.LN_CONTROL_POINT to "Ln Control Point",
        BleGattCharacteristicUuids.ELEVATION to "Elevation",
        BleGattCharacteristicUuids.PRESSURE to "Pressure",
        BleGattCharacteristicUuids.TEMPERATURE to "Temperature",
        BleGattCharacteristicUuids.HUMIDITY to "Humidity",
        BleGattCharacteristicUuids.TRUE_WIND_SPEED to "True Wind Speed",
        BleGattCharacteristicUuids.TRUE_WIND_DIRECTION to "True Wind Direction",
        BleGattCharacteristicUuids.APPARENT_WIND_SPEED to "Apparent Wind Speed",
        BleGattCharacteristicUuids.APPARENT_WIND_DIRECTION to "Apparent Wind Direction",
        BleGattCharacteristicUuids.GUST_FACTOR to "Gust Factor",
        BleGattCharacteristicUuids.POLLEN_CONCENTRATION to "Pollen Concetration",
        BleGattCharacteristicUuids.UV_INDEX to "Uv Index",
        BleGattCharacteristicUuids.IRRADIANCE to "Irradiance",
        BleGattCharacteristicUuids.RAINFALL to "Rainfall",
        BleGattCharacteristicUuids.WIND_CHILL to "Wind Chill",
        BleGattCharacteristicUuids.HEAT_INDEX to "Heat Index",
        BleGattCharacteristicUuids.DEW_POINT to "Dew Point",
        BleGattCharacteristicUuids.DESCRIPTOR_VALUE_CHANGED to "Descriptor Value Changed",
        BleGattCharacteristicUuids.AEROBIC_HEART_RATE_LOWER_LIMIT to "Aerobic Heart Rate Lower Limit",
        BleGattCharacteristicUuids.AEROBIC_THRESHOLD to "Aerobic Threshold",
        BleGattCharacteristicUuids.AGE to "Age",
        BleGattCharacteristicUuids.ANAEROBIC_HEART_RATE_LOWER_LIMIT to "Anaerobic Heart Rate Lower Limit",
        BleGattCharacteristicUuids.ANAEROBIC_HEART_RATE_UPPER_LIMIT to "Anaerobic Heart Rate Upper Limit",
        BleGattCharacteristicUuids.ANAEROBIC_THRESHOLD to "Anaerobic Threshold",
        BleGattCharacteristicUuids.AEROBIC_HEART_RATE_UPPER_LIMIT to "Aerobic Heart Rate Upper Limit",
        BleGattCharacteristicUuids.DATE_OF_BIRTH to "Date of Birth",
        BleGattCharacteristicUuids.DATE_OF_THRESHOLD_ASSESSMENT to "Date of Threshold Assessment",
        BleGattCharacteristicUuids.EMAIL_ADDRESS to "Email Address",
        BleGattCharacteristicUuids.FAT_BURN_HEART_RATE_LOWER_LIMIT to "Fat Burn Heart Rate Lower Limit",
        BleGattCharacteristicUuids.FAT_BURN_HEART_RATE_UPPER_LIMIT to "Fat Burn Heart Rate Upper Limit",
        BleGattCharacteristicUuids.FIRST_NAME to "First Name",
        BleGattCharacteristicUuids.FIVE_ZONE_HEART_RATE_LIMITS to "Five Zone Heart Rate Limits",
        BleGattCharacteristicUuids.GENDER to "Gender",
        BleGattCharacteristicUuids.HEART_RATE_MAX to "Heart Rate Max",
        BleGattCharacteristicUuids.HEIGHT to "Height",
        BleGattCharacteristicUuids.HIP_CIRCUMFERENCE to "Hip Circumference",
        BleGattCharacteristicUuids.LAST_NAME to "Last Name",
        BleGattCharacteristicUuids.MAXIMUM_RECOMMENDED_HEART_RATE to "Maximum Recommended Heart Rate",
        BleGattCharacteristicUuids.RESTING_HEART_RATE to "Resting Heart Rate",
        BleGattCharacteristicUuids.SPORT_TYPE_FOR_AEROBIC_AND_ANAEROBIC_THRESHOLDS to "Short Type For Aerobic And Anaerobic Thresholds",
        BleGattCharacteristicUuids.THREE_ZONE_HEART_RATE_LIMITS to "Three Zone Heart Rate Limits",
        BleGattCharacteristicUuids.TWO_ZONE_HEART_RATE_LIMIT to "Two Zone Heart Rate Limit",
        BleGattCharacteristicUuids.VO2_MAX to "VO2 Max",
        BleGattCharacteristicUuids.WAIST_CIRCUMFERENCE to "Waist Circumference",
        BleGattCharacteristicUuids.WEIGHT to "Weight",
        BleGattCharacteristicUuids.BOND_MANAGEMENT_FEATURE to "Bond Managerment Feature",
        BleGattCharacteristicUuids.CENTRAL_ADDRESS_RESOLUTION to "Central Address Resolution",
        BleGattCharacteristicUuids.CGM_MEASUREMENT to "GCM Measurement",
        BleGattCharacteristicUuids.CGM_FEATURE to "GCM Feature",
        BleGattCharacteristicUuids.CGM_STATUS to "GCM Status",
        BleGattCharacteristicUuids.CGM_SESSION_START_TIME to "GCM Session Start Time",
        BleGattCharacteristicUuids.CGM_SESSION_RUN_TIME to "GCM Session Run Time",
        BleGattCharacteristicUuids.CGM_SPECIFIC_OPS_CONTROL_POINT to "GCM Specific Ops Control Point",
        BleGattCharacteristicUuids.INDOOR_POSITIONING_CONFIGURATION to "Indoor Positioning Configuration",
        BleGattCharacteristicUuids.LATITUDE to "Latitude",
        BleGattCharacteristicUuids.LONGITUDE to "Longitude",
        BleGattCharacteristicUuids.LOCAL_NORTH_COORDINATE to "Local North Coordinate",
        BleGattCharacteristicUuids.LOCAL_EAST_COORDINATE to "Local East Coordinate",
        BleGattCharacteristicUuids.FLOOR_NUMBER to "Floor Number",
        BleGattCharacteristicUuids.ALTITUDE to "Altitude",
        BleGattCharacteristicUuids.DATABASE_CHANGE_INCREMENT to "Database Change Increment",
        BleGattCharacteristicUuids.USER_INDEX to "User Index",
        BleGattCharacteristicUuids.BODY_COMPOSITION_FEATURE to "Body Composition Feature",
        BleGattCharacteristicUuids.BODY_COMPOSITION_MEASUREMENT to "Body Composition Measurement",
        BleGattCharacteristicUuids.WEIGHT_MEASUREMENT to "Weight Measurement",
        BleGattCharacteristicUuids.WEIGHT_SCALE_FEATURE to "Weight Scale Feature",
        BleGattCharacteristicUuids.USER_CONTROL_POINT to "User Control Point",
        BleGattCharacteristicUuids.MAGNETIC_FLUX_DENSITY_2D to "Magnetic Flux Density 2D",
        BleGattCharacteristicUuids.MAGNETIC_FLUX_DENSITY_3D to "Magnetic Flux Density 3D",
        BleGattCharacteristicUuids.LANGUAGE to "Language",
        BleGattCharacteristicUuids.BAROMETRIC_PRESSURE_TREND to "Barometric Pressure Trend",
        BleGattCharacteristicUuids.BOND_MANAGEMENT_CONTROL_POINT to "Bond Management Control Point",
        BleGattCharacteristicUuids.UNCERTAINTY to "Uncertainty",
        BleGattCharacteristicUuids.LOCATION_NAME to "Location Name",
        BleGattCharacteristicUuids.URI to "Ui",
        BleGattCharacteristicUuids.HTTP_HEADERS to "Http Headers",
        BleGattCharacteristicUuids.HTTP_STATUS_CODE to "Http Status Code",
        BleGattCharacteristicUuids.HTTP_ENTITY_BODY to "Http Entity Body",
        BleGattCharacteristicUuids.HTTP_CONTROL_POINT to "Http Control Point",
        BleGattCharacteristicUuids.HTTPS_SECURITY to "Https Security",
        BleGattCharacteristicUuids.TDS_CONTROL_POINT to "Tds Control Point",
        BleGattCharacteristicUuids.OTS_FEATURE to "Ots Feature",
        BleGattCharacteristicUuids.OBJECT_NAME to "Object Name",
        BleGattCharacteristicUuids.OBJECT_TYPE to "Object Type",
        BleGattCharacteristicUuids.OBJECT_SIZE to "Object Size",
        BleGattCharacteristicUuids.OBJECT_FIRST_CREATED to "Object First Created",
        BleGattCharacteristicUuids.OBJECT_LAST_MODIFIED to "Object Last Modified",
        BleGattCharacteristicUuids.OBJECT_ID to "Object Id",
        BleGattCharacteristicUuids.OBJECT_PROPERTIES to "Object Properties",
        BleGattCharacteristicUuids.OBJECT_ACTION_CONTROL_POINT to "Object Action Control Point",
        BleGattCharacteristicUuids.OBJECT_LIST_CONTROL_POINT to "Object List Control Point",
        BleGattCharacteristicUuids.OBJECT_LIST_FILTER to "Object List Filter",
        BleGattCharacteristicUuids.OBJECT_CHANGED to "Object Changed",
        BleGattCharacteristicUuids.RESOLVABLE_PRIVATE_ADDRESS_ONLY to "Resolvable Private Address Only",
        BleGattCharacteristicUuids.UNSPECIFIED to "Unspecified",
        BleGattCharacteristicUuids.DIRECTORY_LISTING to "Directory Listing",
        BleGattCharacteristicUuids.FITNESS_MACHINE_FEATURE to "Fitness Machine Feature",
        BleGattCharacteristicUuids.TREADMILL_DATA to "Treadmill Data",
        BleGattCharacteristicUuids.CROSS_TRAINER_DATA to "Cross Trainer Data",
        BleGattCharacteristicUuids.STEP_CLIMBER_DATA to "Step Climber Data",
        BleGattCharacteristicUuids.STAIR_CLIMBER_DATA to "Stair Climber Data",
        BleGattCharacteristicUuids.ROWER_DATA to "Rower Data",
        BleGattCharacteristicUuids.INDOOR_BIKE_DATA to "Indoor Bike Data",
        BleGattCharacteristicUuids.TRAINING_STATUS to "Training Status",
        BleGattCharacteristicUuids.SUPPORTED_SPEED_RANGE to "Supported Speed Range",
        BleGattCharacteristicUuids.SUPPORTED_INCLINATION_RANGE to "Supported Inclination Range",
        BleGattCharacteristicUuids.SUPPORTED_RESISTANCE_LEVEL_RANGE to "Supported Resistance Level Range",
        BleGattCharacteristicUuids.SUPPORTED_HEART_RATE_RANGE to "Supported Heart Rate Range",
        BleGattCharacteristicUuids.SUPPORTED_POWER_RANGE to "Supported Power Range",
        BleGattCharacteristicUuids.FITNESS_MACHINE_CONTROL_POINT to "Fitness Machine Control Point",
        BleGattCharacteristicUuids.FITNESS_MACHINE_STATUS to "Fitness Machine Status",
        BleGattCharacteristicUuids.MESH_PROVISIONING_DATA_IN to "Mesh Provisioning Date in",
        BleGattCharacteristicUuids.MESH_PROVISIONING_DATA_OUT to "Mesh Provisioning Date out",
        BleGattCharacteristicUuids.MESH_PROXY_DATA_IN to "Mesh Proxy Data Int",
        BleGattCharacteristicUuids.MESH_PROXY_DATA_OUT to "Mesh Proxy Data Out",
        BleGattCharacteristicUuids.AVERAGE_CURRENT to "Average Current",
        BleGattCharacteristicUuids.AVERAGE_VOLTAGE to "Average Voltage",
        BleGattCharacteristicUuids.BOOLEAN to "Boolean",
        BleGattCharacteristicUuids.CHROMATIC_DISTANCE_FROM_PLANCKIAN to "Chromatic Distance From Plackian",
        BleGattCharacteristicUuids.CHROMATICITY_COORDINATES to "Chromaticity Coordinates",
        BleGattCharacteristicUuids.CHROMATICITY_IN_CCT_AND_DUV_VALUES to "Chromaticity in Cct And Duv Names",
        BleGattCharacteristicUuids.CHROMATICITY_TOLERANCE to "Chromaticity Tolerance",
        BleGattCharacteristicUuids.CIE_13_3_1995_COLOR_RENDERING_INDEX to "Cie 13 3 1995 Color Rendering Index",
        BleGattCharacteristicUuids.COEFFICIENT to "Coefficient",
        BleGattCharacteristicUuids.CORRELATED_COLOR_TEMPERATURE to "Correlated Color Temperature",
        BleGattCharacteristicUuids.COUNT_16 to "Count 16",
        BleGattCharacteristicUuids.COUNT_24 to "Count 24",
        BleGattCharacteristicUuids.COUNTRY_CODE to "Country Code",
        BleGattCharacteristicUuids.DATE_UTC to "Date Utc",
        BleGattCharacteristicUuids.ELECTRIC_CURRENT to "Electric Current",
        BleGattCharacteristicUuids.ELECTRIC_CURRENT_RANGE to "Electric Current Range",
        BleGattCharacteristicUuids.ELECTRIC_CURRENT_SPECIFICATION to "Electric Current Specification",
        BleGattCharacteristicUuids.ELECTRIC_CURRENT_STATISTICS to "Electric Current Statistics",
        BleGattCharacteristicUuids.ENERGY to "Energy",
        BleGattCharacteristicUuids.ENERGY_IN_A_PERIOD_OF_DAY to "Energy In A Period Of Day",
        BleGattCharacteristicUuids.EVENT_STATISTICS to "Event Statistics",
        BleGattCharacteristicUuids.FIXED_STRING_16 to "Fixed String 16",
        BleGattCharacteristicUuids.FIXED_STRING_24 to "Fixed String 24",
        BleGattCharacteristicUuids.FIXED_STRING_36 to "Fixed String 36",
        BleGattCharacteristicUuids.FIXED_STRING_8 to "Fixed String 8",
        BleGattCharacteristicUuids.GENERIC_LEVEL to "Generic Level",
        BleGattCharacteristicUuids.GLOBAL_TRADE_ITEM_NUMBER to "Global Trade Item Number",
        BleGattCharacteristicUuids.ILLUMINANCE to "Illuminance",
        BleGattCharacteristicUuids.LUMINOUS_EFFICACY to "Luminous Efficacy",
        BleGattCharacteristicUuids.LUMINOUS_ENERGY to "Luminous Energy",
        BleGattCharacteristicUuids.LUMINOUS_EXPOSURE to "Luminous Exposure",
        BleGattCharacteristicUuids.LUMINOUS_FLUX to "Luminous Flux",
        BleGattCharacteristicUuids.LUMINOUS_FLUX_RANGE to "Luminous Flux Range",
        BleGattCharacteristicUuids.LUMINOUS_INTENSITY to "Luminous Intensity",
        BleGattCharacteristicUuids.MASS_FLOW to "Mass Flow",
        BleGattCharacteristicUuids.PERCEIVED_LIGHTNESS to "Perceived Lightness",
        BleGattCharacteristicUuids.PERCENTAGE_8 to "Percentage 8",
        BleGattCharacteristicUuids.POWER to "Power",
        BleGattCharacteristicUuids.POWER_SPECIFICATION to "Power Specification",
        BleGattCharacteristicUuids.RELATIVE_RUNTIME_IN_A_CURRENT_RANGE to "Relative Runtime In A Current Range",
        BleGattCharacteristicUuids.RELATIVE_RUNTIME_IN_A_GENERIC_LEVEL_RANGE to "Relative Runtime In A Generic Level Range",
        BleGattCharacteristicUuids.RELATIVE_VALUE_IN_A_VOLTAGE_RANGE to "Relative Value In A Voltage Range",
        BleGattCharacteristicUuids.RELATIVE_VALUE_IN_AN_ILLUMINANCE_RANGE to "Relative Value In An Illuminance Range",
        BleGattCharacteristicUuids.RELATIVE_VALUE_IN_A_PERIOD_OF_DAY to "Relative Value In A Period Of Day",
        BleGattCharacteristicUuids.RELATIVE_VALUE_IN_A_TEMPERATURE_RANGE to "Relative Value In A Temperature Range",
        BleGattCharacteristicUuids.TEMPERATURE_8 to "Temperature 8",
        BleGattCharacteristicUuids.TEMPERATURE_8_IN_A_PERIOD_OF_DAY to "Temperature 8 In A Period Of Day",
        BleGattCharacteristicUuids.TEMPERATURE_8_STATISTICS to "Temperature 8 Statistics",
        BleGattCharacteristicUuids.TEMPERATURE_RANGE to "Temperature Range",
        BleGattCharacteristicUuids.TEMPERATURE_STATISTICS to "Temperature Statistics",
        BleGattCharacteristicUuids.TIME_DECIHOUR_8 to "Time Decihour 8",
        BleGattCharacteristicUuids.TIME_EXPONENTIAL_8 to "Time Exponential 8",
        BleGattCharacteristicUuids.TIME_HOUR_24 to "Time Hour 24",
        BleGattCharacteristicUuids.TIME_MILLISECOND_24 to "Time Millisecond 24",
        BleGattCharacteristicUuids.TIME_SECOND_16 to "Time Second 16",
        BleGattCharacteristicUuids.TIME_SECOND_8 to "Time Second 8",
        BleGattCharacteristicUuids.VOLTAGE to "Voltage",
        BleGattCharacteristicUuids.VOLTAGE_SPECIFICATION to "Voltage Specification",
        BleGattCharacteristicUuids.VOLTAGE_STATISTICS to "Voltage Statistics",
        BleGattCharacteristicUuids.VOLUME_FLOW to "Volume Flow",
        BleGattCharacteristicUuids.CHROMATICITY_COORDINATE to "Chromaticity Coordinate",
        BleGattCharacteristicUuids.RC_FEATURE to "Rc Feature",
        BleGattCharacteristicUuids.RC_SETTINGS to "Rc Settings",
        BleGattCharacteristicUuids.RECONNECTION_CONFIGURATION_CONTROL_POINT to "Reconnection Configuration Control Point",
        BleGattCharacteristicUuids.IDD_STATUS_CHANGED to "Idd Status Changed",
        BleGattCharacteristicUuids.IDD_STATUS to "Idd Status",
        BleGattCharacteristicUuids.IDD_ANNUNCIATION_STATUS to "Idd Annunciation Status",
        BleGattCharacteristicUuids.IDD_FEATURES to "Idd Features",
        BleGattCharacteristicUuids.IDD_STATUS_READER_CONTROL_POINT to "Idd Status Reader Control Point",
        BleGattCharacteristicUuids.IDD_COMMAND_CONTROL_POINT to "Idd Command Control Point",
        BleGattCharacteristicUuids.IDD_COMMAND_DATA to "Idd Command Data",
        BleGattCharacteristicUuids.IDD_RECORD_ACCESS_CONTROL_POINT to "Idd Record Access Control Point",
        BleGattCharacteristicUuids.IDD_HISTORY_DATA to "Idd History Date",
        BleGattCharacteristicUuids.CLIENT_SUPPORTED_FEATURES to "Client Supported Features",
        BleGattCharacteristicUuids.DATABASE_HASH to "Database Hash",
        BleGattCharacteristicUuids.BSS_CONTROL_POINT to "Bss Control Point",
        BleGattCharacteristicUuids.BSS_RESPONSE to "Bss Response",
        BleGattCharacteristicUuids.EMERGENCY_ID to "Emergency Id",
        BleGattCharacteristicUuids.EMERGENCY_TEXT to "Emergency Text",
        BleGattCharacteristicUuids.ENHANCED_BLOOD_PRESSURE_MEASUREMENT to "Enhanced Blood Pressure Measurement",
        BleGattCharacteristicUuids.ENHANCED_INTERMEDIATE_CUFF_PRESSURE to "Enhanced Intermediate Cuff Pressure",
        BleGattCharacteristicUuids.BLOOD_PRESSURE_RECORD to "Blood Pressure",
        BleGattCharacteristicUuids.BR_EDR_HANDOVER_DATA to "Br Edr Handover Data",
        BleGattCharacteristicUuids.BLUETOOTH_SIG_DATA to "Bluetooth Sig Data",
        BleGattCharacteristicUuids.SERVER_SUPPORTED_FEATURES to "Server Supported Features",
        BleGattCharacteristicUuids.PHYSICAL_ACTIVITY_MONITOR_FEATURES to "Physical",
        BleGattCharacteristicUuids.GENERAL_ACTIVITY_INSTANTANEOUS_DATA to "General Activity Instantaneous Data",
        BleGattCharacteristicUuids.GENERAL_ACTIVITY_SUMMARY_DATA to "General Activity Summary Data",
        BleGattCharacteristicUuids.CARDIORESPIRATORY_ACTIVITY_INSTANTANEOUS_DATA to "Cardiorespiratory Activity Instantaneous Data",
        BleGattCharacteristicUuids.CARDIORESPIRATORY_ACTIVITY_SUMMARY_DATA to "Cardiorespiratory Activity Summary Data",
        BleGattCharacteristicUuids.STEP_COUNTER_ACTIVITY_SUMMARY_DATA to "Step Counter Activity Summary Data",
        BleGattCharacteristicUuids.SLEEP_ACTIVITY_INSTANTANEOUS_DATA to "Sleep Activity Instantaneous Data",
        BleGattCharacteristicUuids.SLEEP_ACTIVITY_SUMMARY_DATA to "Sleep Activity Summary Data",
        BleGattCharacteristicUuids.PHYSICAL_ACTIVITY_MONITOR_CONTROL_POINT to "2B43",
        BleGattCharacteristicUuids.CURRENT_SESSION to "Current Session",
        BleGattCharacteristicUuids.SESSION to "Session",
        BleGattCharacteristicUuids.PREFERRED_UNITS to "Preferred Units",
        BleGattCharacteristicUuids.HIGH_RESOLUTION_HEIGHT to "High Resolution Height",
        BleGattCharacteristicUuids.MIDDLE_NAME to "Middle Name",
        BleGattCharacteristicUuids.STRIDE_LENGTH to "Stride Length",
        BleGattCharacteristicUuids.HANDEDNESS to "Handedness",
        BleGattCharacteristicUuids.DEVICE_WEARING_POSITION to "Device Wearing Position",
        BleGattCharacteristicUuids.FOUR_ZONE_HEART_RATE_LIMITS to "Four Zone Heart Rate Limits",
        BleGattCharacteristicUuids.HIGH_INTENSITY_EXERCISE_THRESHOLD to "High Intensity Exercise Threshold",
        BleGattCharacteristicUuids.ACTIVITY_GOAL to "Activity Goal",
        BleGattCharacteristicUuids.SEDENTARY_INTERVAL_NOTIFICATION to "Sedentary Interval Notification",
        BleGattCharacteristicUuids.CALORIC_INTAKE to "Caloric Intake",
        BleGattCharacteristicUuids.AUDIO_INPUT_STATE to "Audio Input State",
        BleGattCharacteristicUuids.GAIN_SETTINGS_ATTRIBUTE to "Gain Settings Attribute",
        BleGattCharacteristicUuids.AUDIO_INPUT_TYPE to "Audio Input Type",
        BleGattCharacteristicUuids.AUDIO_INPUT_STATUS to "Audion Input Status",
        BleGattCharacteristicUuids.AUDIO_INPUT_CONTROL_POINT to "Audio Input Control Point",
        BleGattCharacteristicUuids.AUDIO_INPUT_DESCRIPTION to "Audio Input Description",
        BleGattCharacteristicUuids.VOLUME_STATE to "Volume State",
        BleGattCharacteristicUuids.VOLUME_CONTROL_POINT to "Volume Control Point",
        BleGattCharacteristicUuids.VOLUME_FLAGS to "Volume Flags",
        BleGattCharacteristicUuids.OFFSET_STATE to "Offset State",
        BleGattCharacteristicUuids.AUDIO_LOCATION to "Audio Location",
        BleGattCharacteristicUuids.VOLUME_OFFSET_CONTROL_POINT to "Volume Offset Control Point",
        BleGattCharacteristicUuids.AUDIO_OUTPUT_DESCRIPTION to "Audio Output Description",
        BleGattCharacteristicUuids.DEVICE_TIME_FEATURE to "Device Time Feature",
        BleGattCharacteristicUuids.DEVICE_TIME_PARAMETERS to "Device Time Parameters",
        BleGattCharacteristicUuids.DEVICE_TIME to "Device Time",
        BleGattCharacteristicUuids.DEVICE_TIME_CONTROL_POINT to "Device Time Control Point",
        BleGattCharacteristicUuids.TIME_CHANGE_LOG_DATA to "Time Change Log Data",
    )
}

/**
 * The bluetooth gatt descriptor uuids for logging purposes.
 */
internal val gattDescriptorUuids by lazy {
    mapOf(
        BleGattDescriptorUuids.CHARACTERISTIC_EXTENDED_PROPERTIES to "Characteristic Extended Properties",
        BleGattDescriptorUuids.CHARACTERISTIC_USER_DESCRIPTION to "Characteristic User Description",
        BleGattDescriptorUuids.CLIENT_CHARACTERISTIC_CONFIGURATION to "Client Characteristic Configuration",
        BleGattDescriptorUuids.SERVER_CHARACTERISTIC_CONFIGURATION to "Server Characteristic Configuration",
        BleGattDescriptorUuids.CHARACTERISTIC_PRESENTATION_FORMAT to "Characteristic Presentation Format",
        BleGattDescriptorUuids.CHARACTERISTIC_AGGREGATE_FORMAT to "Characteristic Aggregate Format",
        BleGattDescriptorUuids.VALID_RANGE to "Valid Range",
        BleGattDescriptorUuids.EXTERNAL_REPORT_REFERENCE to "External Report Reference",
        BleGattDescriptorUuids.REPORT_REFERENCE to "Report Reference",
        BleGattDescriptorUuids.NUMBER_OF_DIGITALS to "Number of Digitals",
        BleGattDescriptorUuids.VALUE_TRIGGER_SETTING to "Value Trigger Setting",
        BleGattDescriptorUuids.ENVIRONMENTAL_SENSING_CONFIGURATION to "Environmental Sensing Configuration",
        BleGattDescriptorUuids.ENVIRONMENTAL_SENSING_MEASUREMENT to "Environmental Sensing Measurement",
        BleGattDescriptorUuids.ENVIRONMENTAL_SENSING_TRIGGER_SETTING to "Environmental Sensing Trigger Setting",
        BleGattDescriptorUuids.TIME_TRIGGER_SETTING to "Time Trigger Setting",
        BleGattDescriptorUuids.COMPLETE_BR_EDR_TRANSPORT_BLOCK_DATA to "Complete Br Edr Transport Block Data",
    )
}
