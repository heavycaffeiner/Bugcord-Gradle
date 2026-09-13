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
        // Holds the dex2jar build the APK to jar transform relies on, fetched by tools/fetch-deps.sh.
        maven { url = uri("local-repo") }
        mavenLocal()
    }
}

rootProject.name = "gradle-plugin"
