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

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

enum class FlavorDimension {
    App,
}

// The content for the App can either come from local static data which is useful for demo
// purposes, or from a production backend server which supplies up-to-date, real content.
// These two product flavors reflect this behaviour.
enum class Flavor(
    val dimension: FlavorDimension,
    val suffix: String? = null,
) {
    Patient(FlavorDimension.App, suffix = ".patient"),
    Staff(FlavorDimension.App, suffix = ".staff"),
}

fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: Flavor) -> Unit = {},
) {
    commonExtension.apply {
        FlavorDimension.values().forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name.lowercase()
        }

        productFlavors {
            Flavor.values().forEach { niaFlavor ->
                register(niaFlavor.name.lowercase()) {
                    dimension = niaFlavor.dimension.name.lowercase()
                    flavorConfigurationBlock(this, niaFlavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (niaFlavor.suffix != null) {
                            applicationIdSuffix = niaFlavor.suffix
                            versionNameSuffix = niaFlavor.suffix
                        }
                    }
                }
            }
        }
    }
}
