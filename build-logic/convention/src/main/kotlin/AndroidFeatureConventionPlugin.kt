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

import com.android.build.gradle.LibraryExtension
import dev.alimansour.shared.plugins.findPlugin
import dev.alimansour.shared.plugins.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "convention.android.library")
            apply(plugin = "convention.koin")
            pluginManager.apply(findPlugin("kotlin-serialization"))

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
            }

            dependencies {
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:data"))

                val bom = libs.findLibrary("androidx-compose-bom").get()
                "implementation"(platform(bom))
                "implementation"(libs.findBundle("compose").get())
                "implementation"(libs.findBundle("coil").get())
                "implementation"(libs.findBundle("appcompat").get())
                "implementation"(libs.findLibrary("androidx.tracing.ktx").get())
                "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
                "implementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())

                "testImplementation"(libs.findBundle("domain.test").get())
                "androidTestImplementation"(platform(bom))
                "androidTestImplementation"(libs.findBundle("app.test").get())
                "debugImplementation"(libs.findLibrary("androidx.compose.ui.tooling").get())
                "debugImplementation"(libs.findLibrary("androidx.ui.test.junit4").get())
            }
        }
    }
}
