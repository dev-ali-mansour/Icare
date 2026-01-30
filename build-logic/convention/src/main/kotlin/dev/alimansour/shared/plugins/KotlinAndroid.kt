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

package dev.alimansour.shared.plugins

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension) {
    commonExtension.apply {
        pluginManager.apply(findPlugin("kotlin-serialization"))
        pluginManager.apply(findPlugin("ktlint"))
        pluginManager.apply(findPlugin("detekt"))

        compileSdk = COMPILE_SDK_VERSION

        defaultConfig.apply {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            minSdk = MIN_SDK_VERSION
        }

        compileOptions.apply {
            // Up to Java 21 APIs are available through desugaring
            // https://developer.android.com/studio/write/java11-minimal-support-table
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
            isCoreLibraryDesugaringEnabled = true
        }
    }

    configureBaseKotlinCompilerOptions()

    dependencies {
        "implementation"(libs.findLibrary("coroutine.core").get())
        "implementation"(libs.findLibrary("timber").get())
        "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
        "coreLibraryDesugaring"(libs.findLibrary("android.desugarJdkLibs").get())
    }
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        // Up to Java 21 APIs are available through desugaring
        // https://developer.android.com/studio/write/java11-minimal-support-table
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    configureBaseKotlinCompilerOptions()
}

private fun Project.configureBaseKotlinCompilerOptions() {
    val configure: Project.() -> Unit = {
        val warningsAsErrors =
            providers
                .gradleProperty("warningsAsErrors")
                .map { it.toBoolean() }
                .orElse(false)

        tasks.withType(KotlinCompile::class.java).configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_21)
                allWarningsAsErrors.set(warningsAsErrors)
                freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
                freeCompilerArgs.add(
                    // Remove this arg after Phase 3.
                    // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-consistent-copy-visibility/#deprecation-timeline
                    "-Xconsistent-data-class-copy-visibility",
                )
            }
        }
    }

    pluginManager.withPlugin("org.jetbrains.kotlin.android") { configure() }
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") { configure() }
    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") { configure() }
}
