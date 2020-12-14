plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
}

android {
    compileSdkVersion(AndroidConfiguration.compileSdk)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId("com.steleot.blekotlin")
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
}

dependencies {

    implementation(Libraries.fragment)
    implementation(Libraries.material)
    implementation(Libraries.recyclerView)
    implementation(Libraries.timber)

    testImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.androidJunit)
}