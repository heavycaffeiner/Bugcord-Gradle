@file:Suppress("UnstableApiUsage")

plugins {
    `kotlin-dsl`
    alias(libs.plugins.publish)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    explicitApi()
    jvmToolchain(21)
}

dependencies {
    compileOnly(libs.android.gradle)
    compileOnly(libs.android.repository)
    compileOnly(libs.android.sdk)
    compileOnly(libs.android.sdklib)

    implementation(libs.dex2jar)
    implementation(libs.jadx.core)
    implementation(libs.jadx.dexInput)
    implementation(libs.kotlinx.serialization)
}

gradlePlugin {
    plugins {
        create("bugcord-plugin") {
            id = "com.bugcord.plugin"
            implementationClass = "com.bugcord.gradle.plugins.BugcordPluginGradle"
        }
        create("bugcord-core") {
            id = "com.bugcord.core"
            implementationClass = "com.bugcord.gradle.plugins.BugcordCoreGradle"
        }
        create("bugcord-injector") {
            id = "com.bugcord.injector"
            implementationClass = "com.bugcord.gradle.plugins.BugcordInjectorGradle"
        }
    }
}

version = "2.3.2"

mavenPublishing {
    coordinates("com.bugcord", "gradle")
    configureBasedOnAppliedPlugins()
}
