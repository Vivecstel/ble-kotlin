import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin(BuildPlugins.kotlinJvm) version Versions.kotlin
    id(BuildPlugins.benManes) version Versions.benManes
    id(BuildPlugins.detekt) version Versions.detekt
}

buildscript {
    val kotlin_version by extra("1.4.20")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(GradleLibraries.androidPlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
    checkForGradleUpdate = true
    outputDir = "${rootProject.buildDir}/dependencyUpdates"
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

val detektAll by tasks.registering(Detekt::class) {
    description = "Runs over whole code base without the starting overhead for each module."
    parallel = true
    buildUponDefaultConfig = true
    setSource(files(projectDir))
    config.from(files(project.rootDir.resolve("config/detekt-config.yml")))
    include("**/*.kt", "**/*.kts")
    exclude("**/resources/**", "**/build/**")
    baseline.set(project.rootDir.resolve("config/detekt-baseline.xml"))
    reports {
        xml.enabled = false
        html {
            enabled = true
            destination = file("${rootProject.buildDir}/reports/detekt-report.html")
        }
        txt.enabled = false
    }
}