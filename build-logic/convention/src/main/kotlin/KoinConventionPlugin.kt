/*
 * Copyright [2025] [Ali Mansour]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file contains modified code originally from the Now in Android project by Google.
 * Original source: https://github.com/android/nowinandroid
 */

import com.google.devtools.ksp.gradle.KspExtension
import dev.alimansour.shared.plugins.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<KspExtension> {
                arg("KOIN_DEFAULT_MODULE", "false")
            }

            dependencies {
                "implementation"(platform(libs.findLibrary("koin.bom").get()))
                "implementation"(libs.findBundle("koin.core").get())
                "ksp"(libs.findLibrary("koin.ksp.compiler").get())
            }

            pluginManager.withPlugin("org.jetbrains.kotlin.android") {
                dependencies {
                    "implementation"(libs.findBundle("koin.android").get())
                }
            }

            pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
                dependencies {
                    "implementation"(libs.findLibrary("koin.core").get())
                }
            }
        }
}
