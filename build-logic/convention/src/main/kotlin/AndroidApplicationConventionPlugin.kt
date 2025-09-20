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

import com.android.build.api.dsl.ApplicationExtension
import com.google.devtools.ksp.gradle.KspExtension
import dev.alimansour.shared.plugins.TARGET_SDK_VERSION
import dev.alimansour.shared.plugins.configureDetekt
import dev.alimansour.shared.plugins.configureKotlinAndroid
import dev.alimansour.shared.plugins.configureKtlint
import dev.alimansour.shared.plugins.findPlugin
import dev.alimansour.shared.plugins.getSecret
import dev.alimansour.shared.plugins.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.invoke
import java.io.File

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(findPlugin("android-application"))
            pluginManager.apply(findPlugin("kotlin-android"))
            pluginManager.apply(findPlugin("gms"))
            pluginManager.apply(findPlugin("ksp"))
            pluginManager.apply(findPlugin("dependency-guard"))
            pluginManager.apply(findPlugin("kotlin-serialization"))
            pluginManager.apply("convention.android.lint")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureKtlint()
                configureDetekt()
                defaultConfig.targetSdk = TARGET_SDK_VERSION
                testOptions.animationsDisabled = true
                signingConfigs {
                    create("release") {
                        val keystoreFile = rootProject.file("release-key.jks")
                        storeFile = keystoreFile
                        storePassword = project.getSecret("KEYSTORE_PASSWORD")
                        keyAlias = project.getSecret("KEY_ALIAS")
                        keyPassword = project.getSecret("KEY_PASSWORD")
                        enableV1Signing = true
                        enableV2Signing = true
                    }

                    getByName("debug") {
                        storeFile = File(project.rootProject.rootDir, "debug.keystore")
                        storePassword = "android"
                        keyAlias = "androiddebugkey"
                        keyPassword = "android"
                        enableV1Signing = true
                        enableV2Signing = true
                    }
                }
            }

            project.plugins.withId("io.gitlab.arturbosch.detekt") {
                project.extensions.configure<DetektExtension> {
                    source.setFrom("src/main/java", "src/main/kotlin")
                    ignoredBuildTypes = listOf("release")
                }
            }

            project.plugins.withId("com.google.devtools.ksp") {
                project.extensions.configure<KspExtension> {
                    arg("KOIN_CONFIG_CHECK", "true")
                }
            }

            tasks {
                getByPath("preBuild").dependsOn("ktlintFormat").dependsOn("detekt")

                getByName<Delete>("clean") {
                    delete.addAll(
                        listOf(
                            "${rootProject.projectDir}/build/reports/detekt",
                            "${rootProject.projectDir}/detekt/reports",
                        ),
                    )
                }
            }
            dependencies {
                val bom = libs.findLibrary("firebase-bom").get()
                "implementation"(libs.findLibrary("multidex").get())
                "implementation"(libs.findLibrary("splashScreen").get())
                "implementation"(libs.findLibrary("app.update").get())
                "implementation"(libs.findBundle("lifecycle").get())
                "implementation"(platform(bom))
                "implementation"(libs.findBundle("firebase").get())
                "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
            }
        }
    }
}
