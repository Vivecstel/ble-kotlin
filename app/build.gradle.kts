plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
}

android {
    compileSdkVersion(AndroidConfiguration.compileSdk)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId("com.steleot.sample")
        minSdkVersion(AndroidConfiguration.minSdk)
        targetSdkVersion(AndroidConfiguration.targetSdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    lintOptions {
        isAbortOnError = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":blekotlin"))
    implementation(Libraries.kotlinJvm)
    implementation(Libraries.kotlinCoroutines)
    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.coreKtx)
    implementation(Libraries.appCompat)
    implementation(Libraries.lifecycleViewModelKtx)
    implementation(Libraries.lifecycleLiveDataKtx)
    implementation(Libraries.material)
    implementation(Libraries.recyclerView)
    implementation(Libraries.timber)

    testImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.androidJunit)
}