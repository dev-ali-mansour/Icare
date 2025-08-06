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

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import dev.alimansour.shared.plugins.TARGET_SDK_VERSION
import dev.alimansour.shared.plugins.configureFlavors
import dev.alimansour.shared.plugins.configureKotlinAndroid
import dev.alimansour.shared.plugins.disableUnnecessaryAndroidTests
import dev.alimansour.shared.plugins.findPlugin
import dev.alimansour.shared.plugins.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(findPlugin("android-library"))
            pluginManager.apply(findPlugin("kotlin-android"))
            pluginManager.apply("convention.android.lint")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = TARGET_SDK_VERSION
                defaultConfig.consumerProguardFiles("consumer-rules.pro")
                testOptions.animationsDisabled = true
                configureFlavors(this)
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                resourcePrefix =
                    path
                        .split("""\W""".toRegex())
                        .drop(1)
                        .distinct()
                        .joinToString(separator = "_")
                        .lowercase() + "_"
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }
            dependencies {
                "implementation"(libs.findLibrary("androidx.tracing.ktx").get())

                "testImplementation"(libs.findLibrary("kotlin.test").get())
                "androidTestImplementation"(libs.findLibrary("kotlin.test").get())
            }
        }
    }
}
