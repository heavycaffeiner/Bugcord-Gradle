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

package com.bugcord.gradle.plugins

import com.bugcord.gradle.Constants
import com.bugcord.gradle.task.AlignTask
import com.bugcord.gradle.task.adb.DeployPrebuiltTask
import com.bugcord.gradle.task.adb.RestartBugcordTask
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.tasks.bundling.ZipEntryCompression
import org.gradle.kotlin.dsl.register

/**
 * The Gradle plugin used to build Bugcord's core subproject.
 * ID: `com.bugcord.core`
 */
@Suppress("unused")
public abstract class BugcordCoreGradle : BugcordBaseGradle() {
    override fun apply(target: Project) {
        registerTasks(target)
        registerDex2jarTransformer(target)
        deleteLegacyCache(target)
    }

    protected fun registerTasks(project: Project) {
        // Compilation
        val compileDexTask = registerCompileDexTask(project)
        val compileResourcesTask = registerCompileResourcesTask(project)

        // Bundling
        val packageTask = project.tasks.register<Zip>("package") {
            group = Constants.TASK_GROUP_INTERNAL
            entryCompression = ZipEntryCompression.STORED
            isPreserveFileTimestamps = false
            archiveBaseName.set(project.name + "-unaligned")
            archiveVersion.set("")
            destinationDirectory.set(project.layout.buildDirectory.dir("intermediates"))

            val resourcesFile = compileResourcesTask.flatMap { it.outputFile }
            val resourcesFileTree = project.zipTree(resourcesFile)
            val resources = resourcesFile.map {
                if (it.asFile.exists()) {
                    resourcesFileTree
                } else {
                    emptyList()
                }
            }

            from(compileDexTask.map { it.outputs.files.singleFile })
            from(resources) {
                exclude("AndroidManifest.xml")
            }
        }

        val makeTask = project.tasks.register<AlignTask>("make") {
            group = Constants.TASK_GROUP
            inputZip.fileProvider(packageTask.map { it.outputs.files.singleFile })
            outputZip.set(project.layout.buildDirectory.file("outputs/${project.name}.zip"))

            doLast {
                logger.lifecycle("Built Bugcord core at ${outputs.files.singleFile}")
            }
        }

        // Deployment
        val restartBugcordTask = project.tasks.register<RestartBugcordTask>("restartBugcord") {
            group = Constants.TASK_GROUP
        }

        project.tasks.register<DeployPrebuiltTask>("deployWithAdb") {
            group = Constants.TASK_GROUP
            deployType = DeployPrebuiltTask.DeployType.Core
            deployFile.fileProvider(makeTask.map { it.outputs.files.singleFile })
            finalizedBy(restartBugcordTask)
        }
    }
}
