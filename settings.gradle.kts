@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        // Holds the dex2jar build the APK to jar transform relies on.
        mavenLocal()
    }
}

rootProject.name = "gradle-plugin"
