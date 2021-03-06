package com.steleot.blekotlin.constants

/**
 * The available uuids as Strings for the gatt characteristics. This list will updated constantly.
 * Based on [link](https://btprodspecificationrefs.blob.core.windows.net/assigned-values/16-bit%20UUID%20Numbers%20Document.pdf)
 */
object BleGattCharacteristicUuids {

    const val DEVICE_NAME = "2A00"
    const val APPEARANCE = "2A01"
    const val PERIPHERAL_PRIVACY_FLAG = "2A02"
    const val RECONNECTION_ADDRESS = "2A03"
    const val PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "2A04"
    const val SERVICE_CHANGED = "2A05"
    const val ALERT_LEVEL = "2A06"
    const val TX_POWER_LEVEL = "2A07"
    const val DATE_TIME = "2A08"
    const val DAY_OF_WEEK = "2A09"
    const val DAY_DATE_TIME = "2A0A"
    const val EXACT_TIME_256 = "2A0C"
    const val DST_OFFSET = "2A0D"
    const val TIME_ZONE = "2A0E"
    const val LOCAL_TIME_INFORMATION = "2A0F"
    const val TIME_WITH_DST = "2A11"
    const val TIME_ACCURACY = "2A12"
    const val TIME_SOURCE = "2A13"
    const val REFERENCE_TIME_INFORMATION = "2A14"
    const val TIME_UPDATE_CONTROL_POINT = "2A16"
    const val TIME_UPDATE_STATE = "2A17"
    const val GLUCOSE_MEASUREMENT = "2A18"
    const val BATTERY_LEVEL = "2A19"
    const val TEMPERATURE_MEASUREMENT = "2A1C"
    const val TEMPERATURE_TYPE = "2A1D"
    const val INTERMEDIATE_TEMPERATURE = "2A1E"
    const val MEASUREMENT_INTERVAL = "2A21"
    const val BOOT_KEYBOARD_INPUT_REPORT = "2A22"
    const val SYSTEM_ID = "2A23"
    const val MODEL_NUMBER_STRING = "2A24"
    const val SERIAL_NUMBER_STRING = "2A25"
    const val FIRMWARE_REVISION_STRING = "2A26"
    const val HARDWARE_REVISION_STRING = "2A27"
    const val SOFTWARE_REVISION_STRING = "2A28"
    const val MANUFACTURER_NAME_STRING = "2A29"
    const val IEEE_11073_20601_REGULATORY_CERTIFICATION_DATA_LIST = "2A2A"
    const val CURRENT_TIME = "2A2B"
    const val SCAN_REFRESH = "2A31"
    const val BOOT_KEYBOARD_OUTPUT_REPORT = "2A32"
    const val BOOT_MOUSE_INPUT_REPORT = "2A33"
    const val GLUCOSE_MEASUREMENT_CONTEXT = "2A34"
    const val BLOOD_PRESSURE_MEASUREMENT = "2A35"
    const val INTERMEDIATE_CUFF_PRESSURE = "2A36"
    const val HEART_RATE_MEASUREMENT = "2A37"
    const val BODY_SENSOR_LOCATION = "2A38"
    const val HEART_RATE_CONTROL_POINT = "2A39"
    const val ALERT_STATUS = "2A3F"
    const val RINGER_CONTROL_POINT = "2A40"
    const val RINGER_SETTING = "2A41"
    const val ALERT_CATEGORY_ID_BIT_MASK = "2A42"
    const val ALERT_CATEGORY_ID = "2A43"
    const val ALERT_NOTIFICATION_CONTROL_POINT = "2A44"
    const val UNREAD_ALERT_STATUS = "2A45"
    const val NEW_ALERT = "2A46"
    const val SUPPORTED_NEW_ALERT_CATEGORY = "2A47"
    const val SUPPORTED_UNREAD_ALERT_CATEGORY = "2A48"
    const val BLOOD_PRESSURE_FEATURE = "2A49"
    const val HID_INFORMATION = "2A4A"
    const val REPORT_MAP = "2A4B"
    const val HID_CONTROL_POINT = "2A4C"
    const val REPORT = "2A4D"
    const val PROTOCOL_MODE = "2A4E"
    const val SCAN_INTERVAL_WINDOW = "2A4F"
    const val PNP_ID = "2A50"
    const val GLUCOSE_FEATURE = "2A51"
    const val RECORD_ACCESS_CONTROL_POINT = "2A52"
    const val RSC_MEASUREMENT = "2A53"
    const val RSC_FEATURE = "2A54"
    const val SC_CONTROL_POINT = "2A55"
    const val AGGREGATE = "2A5A"
    const val CSC_MEASUREMENT = "2A5B"
    const val CSC_FEATURE = "2A5C"
    const val SENSOR_LOCATION = "2A5D"
    const val PLX_SPOT_CHECK_MEASUREMENT = "2A5E"
    const val PLX_CONTINUOUS_MEASUREMENT = "2A5F"
    const val PLX_FEATURES = "2A60"
    const val CYCLING_POWER_MEASUREMENT = "2A63"
    const val CYCLING_POWER_VECTOR = "2A64"
    const val CYCLING_POWER_FEATURE = "2A65"
    const val CYCLING_POWER_CONTROL_POINT = "2A66"
    const val LOCATION_AND_SPEED = "2A67"
    const val NAVIGATION = "2A68"
    const val POSITION_QUALITY = "2A69"
    const val LN_FEATURE = "2A6A"
    const val LN_CONTROL_POINT = "2A6B"
    const val ELEVATION = "2A6C"
    const val PRESSURE = "2A6D"
    const val TEMPERATURE = "2A6E"
    const val HUMIDITY = "2A6F"
    const val TRUE_WIND_SPEED = "2A70"
    const val TRUE_WIND_DIRECTION = "2A71"
    const val APPARENT_WIND_SPEED = "2A72"
    const val APPARENT_WIND_DIRECTION = "2A73"
    const val GUST_FACTOR = "2A74"
    const val POLLEN_CONCENTRATION = "2A75"
    const val UV_INDEX = "2A76"
    const val IRRADIANCE = "2A77"
    const val RAINFALL = "2A78"
    const val WIND_CHILL = "2A79"
    const val HEAT_INDEX = "2A7A"
    const val DEW_POINT = "2A7B"
    const val DESCRIPTOR_VALUE_CHANGED = "2A7D"
    const val AEROBIC_HEART_RATE_LOWER_LIMIT = "2A7E"
    const val AEROBIC_THRESHOLD = "2A7F"
    const val AGE = "2A80"
    const val ANAEROBIC_HEART_RATE_LOWER_LIMIT = "2A81"
    const val ANAEROBIC_HEART_RATE_UPPER_LIMIT = "2A82"
    const val ANAEROBIC_THRESHOLD = "2A83"
    const val AEROBIC_HEART_RATE_UPPER_LIMIT = "2A84"
    const val DATE_OF_BIRTH = "2A85"
    const val DATE_OF_THRESHOLD_ASSESSMENT = "2A86"
    const val EMAIL_ADDRESS = "2A87"
    const val FAT_BURN_HEART_RATE_LOWER_LIMIT = "2A88"
    const val FAT_BURN_HEART_RATE_UPPER_LIMIT = "2A89"
    const val FIRST_NAME = "2A8A"
    const val FIVE_ZONE_HEART_RATE_LIMITS = "2A8B"
    const val GENDER = "2A8C"
    const val HEART_RATE_MAX = "2A8D"
    const val HEIGHT = "2A8E"
    const val HIP_CIRCUMFERENCE = "2A8F"
    const val LAST_NAME = "2A90"
    const val MAXIMUM_RECOMMENDED_HEART_RATE = "2A91"
    const val RESTING_HEART_RATE = "2A92"
    const val SPORT_TYPE_FOR_AEROBIC_AND_ANAEROBIC_THRESHOLDS = "2A93"
    const val THREE_ZONE_HEART_RATE_LIMITS = "2A94"
    const val TWO_ZONE_HEART_RATE_LIMIT = "2A95"
    const val VO2_MAX = "2A96"
    const val WAIST_CIRCUMFERENCE = "2A97"
    const val WEIGHT = "2A98"
    const val BOND_MANAGEMENT_FEATURE = "2AA5"
    const val CENTRAL_ADDRESS_RESOLUTION = "2AA6"
    const val CGM_MEASUREMENT = "2AA7"
    const val CGM_FEATURE = "2AA8"
    const val CGM_STATUS = "2AA9"
    const val CGM_SESSION_START_TIME = "2AAA"
    const val CGM_SESSION_RUN_TIME = "2AAB"
    const val CGM_SPECIFIC_OPS_CONTROL_POINT = "2AAC"
    const val INDOOR_POSITIONING_CONFIGURATION = "2AAD"
    const val LATITUDE = "2AAE"
    const val LONGITUDE = "2AAF"
    const val LOCAL_NORTH_COORDINATE = "2AB0"
    const val LOCAL_EAST_COORDINATE = "2AB1"
    const val FLOOR_NUMBER = "2AB2"
    const val ALTITUDE = "2AB3"
    const val DATABASE_CHANGE_INCREMENT = "2A99"
    const val USER_INDEX = "2A9A"
    const val BODY_COMPOSITION_FEATURE = "2A9B"
    const val BODY_COMPOSITION_MEASUREMENT = "2A9C"
    const val WEIGHT_MEASUREMENT = "2A9D"
    const val WEIGHT_SCALE_FEATURE = "2A9E"
    const val USER_CONTROL_POINT = "2A9F"
    const val MAGNETIC_FLUX_DENSITY_2D = "2AA0"
    const val MAGNETIC_FLUX_DENSITY_3D = "2AA1"
    const val LANGUAGE = "2AA2"
    const val BAROMETRIC_PRESSURE_TREND = "2AA3"
    const val BOND_MANAGEMENT_CONTROL_POINT = "2AA4"
    const val UNCERTAINTY = "2AB4"
    const val LOCATION_NAME = "2AB5"
    const val URI = "2AB6"
    const val HTTP_HEADERS = "2AB7"
    const val HTTP_STATUS_CODE = "2AB8"
    const val HTTP_ENTITY_BODY = "2AB9"
    const val HTTP_CONTROL_POINT = "2ABA"
    const val HTTPS_SECURITY = "2ABB"
    const val TDS_CONTROL_POINT = "2ABC"
    const val OTS_FEATURE = "2ABD"
    const val OBJECT_NAME = "2ABE"
    const val OBJECT_TYPE = "2ABF"
    const val OBJECT_SIZE = "2AC0"
    const val OBJECT_FIRST_CREATED = "2AC1"
    const val OBJECT_LAST_MODIFIED = "2AC2"
    const val OBJECT_ID = "2AC3"
    const val OBJECT_PROPERTIES = "2AC4"
    const val OBJECT_ACTION_CONTROL_POINT = "2AC5"
    const val OBJECT_LIST_CONTROL_POINT = "2AC6"
    const val OBJECT_LIST_FILTER = "2AC7"
    const val OBJECT_CHANGED = "2AC8"
    const val RESOLVABLE_PRIVATE_ADDRESS_ONLY = "2AC9"
    const val UNSPECIFIED = "2ACA"
    const val DIRECTORY_LISTING = "2ACB"
    const val FITNESS_MACHINE_FEATURE = "2ACC"
    const val TREADMILL_DATA = "2ACD"
    const val CROSS_TRAINER_DATA = "2ACE"
    const val STEP_CLIMBER_DATA = "2ACF"
    const val STAIR_CLIMBER_DATA = "2AD0"
    const val ROWER_DATA = "2AD1"
    const val INDOOR_BIKE_DATA = "2AD2"
    const val TRAINING_STATUS = "2AD3"
    const val SUPPORTED_SPEED_RANGE = "2AD4"
    const val SUPPORTED_INCLINATION_RANGE = "2AD5"
    const val SUPPORTED_RESISTANCE_LEVEL_RANGE = "2AD6"
    const val SUPPORTED_HEART_RATE_RANGE = "2AD7"
    const val SUPPORTED_POWER_RANGE = "2AD8"
    const val FITNESS_MACHINE_CONTROL_POINT = "2AD9"
    const val FITNESS_MACHINE_STATUS = "2ADA"
    const val MESH_PROVISIONING_DATA_IN = "2ADB"
    const val MESH_PROVISIONING_DATA_OUT = "2ADC"
    const val MESH_PROXY_DATA_IN = "2ADD"
    const val MESH_PROXY_DATA_OUT = "2ADE"
    const val AVERAGE_CURRENT = "2AE0"
    const val AVERAGE_VOLTAGE = "2AE1"
    const val BOOLEAN = "2AE2"
    const val CHROMATIC_DISTANCE_FROM_PLANCKIAN = "2AE3"
    const val CHROMATICITY_COORDINATES = "2AE4"
    const val CHROMATICITY_IN_CCT_AND_DUV_VALUES = "2AE5"
    const val CHROMATICITY_TOLERANCE = "2AE6"
    const val CIE_13_3_1995_COLOR_RENDERING_INDEX = "2AE7"
    const val COEFFICIENT = "2AE8"
    const val CORRELATED_COLOR_TEMPERATURE = "2AE9"
    const val COUNT_16 = "2AEA"
    const val COUNT_24 = "2AEB"
    const val COUNTRY_CODE = "2AEC"
    const val DATE_UTC = "2AED"
    const val ELECTRIC_CURRENT = "2AEE"
    const val ELECTRIC_CURRENT_RANGE = "2AEF"
    const val ELECTRIC_CURRENT_SPECIFICATION = "2AF0"
    const val ELECTRIC_CURRENT_STATISTICS = "2AF1"
    const val ENERGY = "2AF2"
    const val ENERGY_IN_A_PERIOD_OF_DAY = "2AF3"
    const val EVENT_STATISTICS = "2AF4"
    const val FIXED_STRING_16 = "2AF5"
    const val FIXED_STRING_24 = "2AF6"
    const val FIXED_STRING_36 = "2AF7"
    const val FIXED_STRING_8 = "2AF8"
    const val GENERIC_LEVEL = "2AF9"
    const val GLOBAL_TRADE_ITEM_NUMBER = "2AFA"
    const val ILLUMINANCE = "2AFB"
    const val LUMINOUS_EFFICACY = "2AFC"
    const val LUMINOUS_ENERGY = "2AFD"
    const val LUMINOUS_EXPOSURE = "2AFE"
    const val LUMINOUS_FLUX = "2AFF"
    const val LUMINOUS_FLUX_RANGE = "2B00"
    const val LUMINOUS_INTENSITY = "2B01"
    const val MASS_FLOW = "2B02"
    const val PERCEIVED_LIGHTNESS = "2B03"
    const val PERCENTAGE_8 = "2B04"
    const val POWER = "2B05"
    const val POWER_SPECIFICATION = "2B06"
    const val RELATIVE_RUNTIME_IN_A_CURRENT_RANGE = "2B07"
    const val RELATIVE_RUNTIME_IN_A_GENERIC_LEVEL_RANGE = "2B08"
    const val RELATIVE_VALUE_IN_A_VOLTAGE_RANGE = "2B09"
    const val RELATIVE_VALUE_IN_AN_ILLUMINANCE_RANGE = "2B0A"
    const val RELATIVE_VALUE_IN_A_PERIOD_OF_DAY = "2B0B"
    const val RELATIVE_VALUE_IN_A_TEMPERATURE_RANGE = "2B0C"
    const val TEMPERATURE_8 = "2B0D"
    const val TEMPERATURE_8_IN_A_PERIOD_OF_DAY = "2B0E"
    const val TEMPERATURE_8_STATISTICS = "2B0F"
    const val TEMPERATURE_RANGE = "2B10"
    const val TEMPERATURE_STATISTICS = "2B11"
    const val TIME_DECIHOUR_8 = "2B12"
    const val TIME_EXPONENTIAL_8 = "2B13"
    const val TIME_HOUR_24 = "2B14"
    const val TIME_MILLISECOND_24 = "2B15"
    const val TIME_SECOND_16 = "2B16"
    const val TIME_SECOND_8 = "2B17"
    const val VOLTAGE = "2B18"
    const val VOLTAGE_SPECIFICATION = "2B19"
    const val VOLTAGE_STATISTICS = "2B1A"
    const val VOLUME_FLOW = "2B1B"
    const val CHROMATICITY_COORDINATE = "2B1C"
    const val RC_FEATURE = "2B1D"
    const val RC_SETTINGS = "2B1E"
    const val RECONNECTION_CONFIGURATION_CONTROL_POINT = "2B1F"
    const val IDD_STATUS_CHANGED = "2B20"
    const val IDD_STATUS = "2B21"
    const val IDD_ANNUNCIATION_STATUS = "2B22"
    const val IDD_FEATURES = "2B23"
    const val IDD_STATUS_READER_CONTROL_POINT = "2B24"
    const val IDD_COMMAND_CONTROL_POINT = "2B25"
    const val IDD_COMMAND_DATA = "2B26"
    const val IDD_RECORD_ACCESS_CONTROL_POINT = "2B27"
    const val IDD_HISTORY_DATA = "2B28"
    const val CLIENT_SUPPORTED_FEATURES = "2B29"
    const val DATABASE_HASH = "2B2A"
    const val BSS_CONTROL_POINT = "2B2B"
    const val BSS_RESPONSE = "2B2C"
    const val EMERGENCY_ID = "2B2D"
    const val EMERGENCY_TEXT = "2B2E"
    const val ENHANCED_BLOOD_PRESSURE_MEASUREMENT = "2B34"
    const val ENHANCED_INTERMEDIATE_CUFF_PRESSURE = "2B35"
    const val BLOOD_PRESSURE_RECORD = "2B36"
    const val BR_EDR_HANDOVER_DATA = "2B38"
    const val BLUETOOTH_SIG_DATA = "2B39"
    const val SERVER_SUPPORTED_FEATURES = "2B3A"
    const val PHYSICAL_ACTIVITY_MONITOR_FEATURES = "2B3B"
    const val GENERAL_ACTIVITY_INSTANTANEOUS_DATA = "2B3C"
    const val GENERAL_ACTIVITY_SUMMARY_DATA = "2B3D"
    const val CARDIORESPIRATORY_ACTIVITY_INSTANTANEOUS_DATA = "2B3E"
    const val CARDIORESPIRATORY_ACTIVITY_SUMMARY_DATA = "2B3F"
    const val STEP_COUNTER_ACTIVITY_SUMMARY_DATA = "2B40"
    const val SLEEP_ACTIVITY_INSTANTANEOUS_DATA = "2B41"
    const val SLEEP_ACTIVITY_SUMMARY_DATA = "2B42"
    const val PHYSICAL_ACTIVITY_MONITOR_CONTROL_POINT = "2B43"
    const val CURRENT_SESSION = "2B44"
    const val SESSION = "2B45"
    const val PREFERRED_UNITS = "2B46"
    const val HIGH_RESOLUTION_HEIGHT = "2B47"
    const val MIDDLE_NAME = "2B48"
    const val STRIDE_LENGTH = "2B49"
    const val HANDEDNESS = "2B4A"
    const val DEVICE_WEARING_POSITION = "2B4B"
    const val FOUR_ZONE_HEART_RATE_LIMITS = "2B4C"
    const val HIGH_INTENSITY_EXERCISE_THRESHOLD = "2B4D"
    const val ACTIVITY_GOAL = "2B4E"
    const val SEDENTARY_INTERVAL_NOTIFICATION = "2B4F"
    const val CALORIC_INTAKE = "2B50"
    const val AUDIO_INPUT_STATE = "2B77"
    const val GAIN_SETTINGS_ATTRIBUTE = "2B78"
    const val AUDIO_INPUT_TYPE = "2B79"
    const val AUDIO_INPUT_STATUS = "2B7A"
    const val AUDIO_INPUT_CONTROL_POINT = "2B7B"
    const val AUDIO_INPUT_DESCRIPTION = "2B7C"
    const val VOLUME_STATE = "2B7D"
    const val VOLUME_CONTROL_POINT = "2B7E"
    const val VOLUME_FLAGS = "2B7F"
    const val OFFSET_STATE = "2B80"
    const val AUDIO_LOCATION = "2B81"
    const val VOLUME_OFFSET_CONTROL_POINT = "2B82"
    const val AUDIO_OUTPUT_DESCRIPTION = "2B83"
    const val DEVICE_TIME_FEATURE = "2B8E"
    const val DEVICE_TIME_PARAMETERS = "2B8F"
    const val DEVICE_TIME = "2B90"
    const val DEVICE_TIME_CONTROL_POINT = "2B91"
    const val TIME_CHANGE_LOG_DATA = "2B92"
}
