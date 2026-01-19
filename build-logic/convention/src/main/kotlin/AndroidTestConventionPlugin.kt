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

import com.android.build.api.dsl.TestExtension
import dev.alimansour.shared.plugins.TARGET_SDK_VERSION
import dev.alimansour.shared.plugins.configureDetekt
import dev.alimansour.shared.plugins.configureKotlinAndroid
import dev.alimansour.shared.plugins.configureKtlint
import dev.alimansour.shared.plugins.findPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(findPlugin("android-test"))

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                configureKtlint()
                configureDetekt()
                defaultConfig.targetSdk = TARGET_SDK_VERSION
            }
        }
    }
}
