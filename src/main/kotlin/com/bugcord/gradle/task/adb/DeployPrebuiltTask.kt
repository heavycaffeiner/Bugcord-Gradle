/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.bugcord.gradle.task.adb

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import org.gradle.work.DisableCachingByDefault
import java.io.File
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private const val REMOTE_BUGCORD_DIR = "/storage/emulated/0/Bugcord"

/**
 * Pushes either an Bugcord core or an Bugcord plugin to the device.
 *
 * - When deploying plugins, the plugin is pushed to `/storage/emulated/0/Bugcord/plugins`, corresponding to the
 * primary Android user's external storage. The plugin is then forcefully enabled by changing Bugcord's settings.
 * - When deploying Bugcord Core, the bundle is pushed to `/storage/emulated/0/Bugcord/Bugcord.zip`, similarly to
 * when pushing plugins. Bugcord's settings are then changed to force enable using the local core bundle.
 */
@DisableCachingByDefault
public abstract class DeployPrebuiltTask : AdbTask() {
    public enum class DeployType {
        Core,
        Plugin,
    }

    @get:Input
    public abstract var deployType: DeployType

    @get:InputFile
    public abstract val deployFile: RegularFileProperty

    @TaskAction
    public fun deploy() {
        createBugcordDirs()

        when (deployType) {
            DeployType.Core -> deployCore(deployFile.get().asFile)
            DeployType.Plugin -> deployPlugin(deployFile.get().asFile)
        }
    }

    private fun deployCore(file: File) {
        runAdbCommand("push", file.absolutePath, "$REMOTE_BUGCORD_DIR/Bugcord.zip")
        editBugcordSettings {
            set(
                JsonPrimitive("AC_from_storage"),
                JsonPrimitive(true),
            )
        }

        logger.lifecycle("Deployed Bugcord core to configured devices")
    }

    private fun deployPlugin(file: File) {
        runAdbCommand("push", file.absolutePath, "$REMOTE_BUGCORD_DIR/plugins/${file.name}")
        editBugcordSettings {
            set(
                JsonPrimitive("AC_PM_${file.nameWithoutExtension}"),
                JsonPrimitive(true),
            )
        }

        logger.lifecycle("Deployed plugin ${file.nameWithoutExtension} to configured devices")
    }

    /**
     * Creates the Bugcord directory on the device along with all the subfolders (plugins, themes, settings).
     */
    protected fun createBugcordDirs() {
        runAdbShell(
            "mkdir",
            "-v", // Verbose
            "-p", // Create all parents
            "'$REMOTE_BUGCORD_DIR/plugins'",
            "'$REMOTE_BUGCORD_DIR/themes'",
            "'$REMOTE_BUGCORD_DIR/settings'",
        )
    }

    /**
     * Reads Bugcord core's settings from the device, then applies [block] to it,
     * and writes it back to the device.
     */
    @OptIn(ExperimentalSerializationApi::class)
    protected fun editBugcordSettings(block: (MutableMap<JsonPrimitive, JsonElement>).() -> Unit) {
        val localSettingsFile = temporaryDir.resolve("settings.json")
        val remoteSettingsPath = "$REMOTE_BUGCORD_DIR/settings/Bugcord.json"

        bugcordSettingsLock.withLock {
            try {
                runAdbCommand("pull", remoteSettingsPath, localSettingsFile.absolutePath)
            } catch (e: AdbException) {
                logger.info("Failed to pull Bugcord settings", e)
            }

            val settings = try {
                Json.decodeFromStream<MutableMap<JsonPrimitive, JsonElement>>(
                    stream = localSettingsFile.inputStream())
            } catch (e: Exception) {
                logger.info("Failed to parse Bugcord settings", e)
                mutableMapOf()
            }

            // Apply modifier block
            block(settings)

            @Suppress("JSON_FORMAT_REDUNDANT")
            val newSettings = Json { prettyPrint = true }.encodeToString(settings)

            localSettingsFile.writeText(newSettings)
            runAdbCommand("push", localSettingsFile.absolutePath, remoteSettingsPath)
        }
    }

    private companion object {
        /**
         * Lock access to the device's `Bugcord.json` settings file to prevent race conditions.
         */
        val bugcordSettingsLock = ReentrantLock(/* fair = */ false)
    }
}
