<div align="center">
    <h1>Bugcord Gradle Plugin</h1>
    <p>The Gradle build plugin for the Bugcord project & plugins </p>
    <span>
        <a href="https://discord.gg/EsNDvBaHVU"><img alt="Discord" src="https://img.shields.io/discord/811255666990907402?logo=discord&logoColor=white&style=for-the-badge&color=5865F2"/></a>
        <a href="https://github.com/Bugcord/Bugcord/actions/workflows/build.yml?query=branch%3Amain"><img alt="Build Status" src="https://img.shields.io/github/actions/workflow/status/Bugcord/gradle-plugin/build.yml?branch=main&label=Build&logo=github&style=for-the-badge"/></a>
        <a href="https://github.com/Bugcord/Bugcord/blob/main/LICENSE"><img alt="License" src="https://img.shields.io/github/license/Bugcord/gradle-plugin?style=for-the-badge&color=181717"/></a>
        <img alt="Code size" src="https://img.shields.io/github/languages/code-size/Bugcord/gradle-plugin?style=for-the-badge&color=181717"/>
    </span>
</div>

## Configuration (plugins)

Please refer to [Bugcord/plugins-template](https://github.com/Bugcord/plugins-template) for a complete
example on how to use this Gradle plugin from the context of an Bugcord plugin.

To add it to your project, publish the plugin to your local Maven repository and add that as a
repository. The examples below assume you are using Gradle's "settings" dependency resolution
management, and Gradle's version catalogs to manage dependency versions.

You will also need to specify the Discord version you will be building your plugins against.
At the moment, only v126.21 (126021) is available.
You will also need to explicitly specify the Kotlin stdlib (if you are using Kotlin) as a `compileOnly` dependency.

```kotlin
// settings.gradle.kts

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
    }
}
```

```toml
# gradle/libs.versions.toml

[versions]
bugcord-gradle = "2.3.1"
android = "8.13.2"
discord = "126021"
kotlin = "2.3.0"

[libraries]
discord = { module = "com.discord:discord", version.ref = "discord" }
kotlin-stdlib = { module = "org.jetbrains.kotlin:kotlin-stdlib", version.ref = "kotlin" }

[plugins]
bugcord-plugin = { id = "com.bugcord.plugin", version.ref = "bugcord-gradle" }
android-library = { id = "com.android.library", version.ref = "android" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
```

You will then need to apply the plugin in your root project, as well as all plugin subprojects.
For plugin subproject, you must also then configure the `bugcord` Gradle extension.

```kotlin
// build.gradle.kts (root)

plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.bugcord.plugin) apply true // Applying is not a mistake!
}
```

```kotlin
// build.gradle.kts (plugins)

plugins {
    alias(libs.plugins.kotlin.android) apply true
    alias(libs.plugins.android.library) apply true
    alias(libs.plugins.bugcord.plugin) apply true
}

dependencies {
    compileOnly(libs.discord)
    compileOnly(libs.kotlin.stdlib)
}

bugcord {
    // ... Refer to plugins-template ...
}
```

If you are applying the plugins to multiple projects at once using `subprojects { ... }`, the syntax will vary.
Please refer to the plugins-template example here:
[`build.gradle.kts`](https://github.com/Bugcord/plugins-template/blob/main/plugins/build.gradle.kts)

## Usage (plugins)

To build plugins using this Gradle plugin as a build tool:

1. Create an entrypoint class extending the `Plugin` class from Bugcord, and is annotated with `@BugcordPlugin`.
2. Run the `make` task for the subproject to build the plugin. If you need the output plugin zip, the task
   will print a path to the built plugin upon completion.
3. To quickly test plugins with a live Bugcord installation (with a physical device or an emulator), run the
   `deployWithAdb` task for the subproject. This will automatically build, deploy, and restart Bugcord for
   you to immediately test your changes.
