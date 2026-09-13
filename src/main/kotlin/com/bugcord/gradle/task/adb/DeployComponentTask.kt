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

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import org.gradle.work.DisableCachingByDefault

/**
 * Pushes a custom component build to a device(s) with Bugcord Manager installed.
 *
 * For example, when deploying Bugcord Injector, the dex is pushed to `/data/local/tmp/bugcord`,
 * an intent is launched, starting Bugcord Manager to import the component to its internal storage.
 * Bugcord Manager then prompts to start a new installation of Bugcord.
 */
@DisableCachingByDefault
public abstract class DeployComponentTask : AdbTask() {
    @get:Input
    public abstract var componentType: String

    @get:InputFile
    public abstract val componentFile: RegularFileProperty

    @get:Input
    public abstract var componentVersion: String

    @TaskAction
    public fun deploy() {
        val componentFile = componentFile.get().asFile
        val timestamp = System.currentTimeMillis()
        val remoteComponentName = "${timestamp}_${componentVersion}.${componentFile.extension}"
        val remoteComponentPath = "/data/local/tmp/$remoteComponentName"

        runAdbCommand("push", componentFile.absolutePath, remoteComponentPath)
        runAdbShell(
            "am", "start",
            "-n", "com.bugcord.manager/.MainActivity",
            "-a", "com.bugcord.manager.IMPORT_COMPONENT",
            "--es", "bugcord.file", "'$remoteComponentName'",
            "--es", "bugcord.componentType", "'$componentType'",
        )

        // Wait a bit to let Bugcord Manager import the component
        Thread.sleep(2000)

        runAdbShell("rm", "-rf", "'$remoteComponentPath'")
    }
}
