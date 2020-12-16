object Libraries {

    /* https://kotlinlang.org/api/latest/jvm/stdlib/ */
    const val kotlinJvm = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"

    /* https://github.com/Kotlin/kotlinx.coroutines */
    const val kotlinCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"

    /* https://developer.android.com/kotlin/coroutines */
    const val kotlinCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    /* https://developer.android.com/jetpack/androidx/releases/activity */
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activity}"

    /* https://developer.android.com/jetpack/androidx/releases/fragment */
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"

    /* https://developer.android.com/jetpack/androidx/releases/core */
    const val coreKtx = "androidx.core:core-ktx:${Versions.core}"

    /* https://developer.android.com/jetpack/androidx/releases/appcompat */
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"

    /* https://developer.android.com/jetpack/androidx/releases/lifecycle */
    const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleLiveDataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    /* https://github.com/material-components/material-components-android */
    const val material = "com.google.android.material:material:${Versions.material}"

    /* https://developer.android.com/jetpack/androidx/releases/recyclerview */
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"

    /* https://github.com/JakeWharton/timber */
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
}
