import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.*

plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.jetbrainsDokka) version Versions.jetbrainsDokka
    id(BuildPlugins.jFrogBintray) version Versions.jFrogBintray
    `maven-publish`
}

android {
    compileSdkVersion(AndroidConfiguration.compileSdk)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdkVersion(AndroidConfiguration.minSdk)
        targetSdkVersion(AndroidConfiguration.targetSdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation(Libraries.kotlinJvm)
    implementation(Libraries.kotlinCoroutines)
    implementation(Libraries.kotlinCoroutinesAndroid)

    testImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.androidJunit)
}

tasks {
    dokkaHtml {
        outputDirectory.set(File("$buildDir/javadoc"))
        moduleName.set(rootProject.name)
    }
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
    dependsOn(tasks.dokkaHtml)
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

publishing {
    publications {
        create<MavenPublication>("library") {
            groupId = PublishLibrary.artifactGroup
            artifactId = PublishLibrary.artifactName
            version = PublishLibrary.artifactVersion
            artifact("$buildDir/outputs/aar/${artifactId}-release.aar")
            artifact(dokkaJar)
            artifact(sourcesJar)

            pom {
                name.set(PublishLibrary.artifactName)
                description.set(PublishLibrary.pomDesc)
                url.set(PublishLibrary.pomUrl)
                licenses {
                    license {
                        name.set(PublishLibrary.pomLicenseName)
                        url.set(PublishLibrary.pomLicenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(PublishLibrary.pomDeveloperId)
                        name.set(PublishLibrary.pomDeveloperName)
                        email.set(PublishLibrary.pomDeveloperEmail)
                    }
                }
                scm {
                    connection.set(PublishLibrary.pomVcsUrl)
                    developerConnection.set(PublishLibrary.pomVcsUrl)
                    url.set(PublishLibrary.pomUrl)
                }
            }
        }
    }
}

bintray {
    user = gradleLocalProperties(rootDir).getProperty("bintray.user")
    key = gradleLocalProperties(rootDir).getProperty("bintray.apikey")
    publish = true
    setPublications("library")
    pkg.apply {
        repo = PublishLibrary.artifactName
        name = PublishLibrary.artifactGroup
        desc = PublishLibrary.pomDesc
        websiteUrl = PublishLibrary.pomUrl
        vcsUrl = PublishLibrary.pomVcsUrl
        setLicenses(PublishLibrary.pomLicence)
        publish = true
        publicDownloadNumbers = true
        version.apply {
            name = PublishLibrary.artifactVersion
            desc = PublishLibrary.pomDesc
            released = Date().toString()
            vcsTag = "v${PublishLibrary.artifactVersion}"
        }
    }
}

