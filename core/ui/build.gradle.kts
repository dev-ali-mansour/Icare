import build.BuildConfig
import test.TestBuildConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "eg.edu.cu.csds.icare.core.ui"
    compileSdk = BuildConfig.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = BuildConfig.MIN_SDK_VERSION

        testInstrumentationRunner = TestBuildConfig.TEST_INSTRUMENTATION_RUNNER
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        JavaVersion.VERSION_21
    }
}

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(libs.timber)
    api(platform(libs.androidx.compose.bom))
    api(libs.bundles.compose)
    api(libs.bundles.coil)
    api(libs.bundles.appcompat)
    api(platform(libs.koin.bom))
    api(libs.bundles.koin)
    api(platform(libs.firebase.bom))
    api(libs.firebase.auth)
    implementation(libs.androidx.browser)
    implementation(libs.kotlinx.serialization.json)

    testApi(libs.bundles.app.test)
    androidTestApi(libs.androidx.ui.test.manifest)
    debugApi(libs.androidx.ui.tooling)
    debugApi(libs.androidx.ui.test.junit4)

    implementation(project(":core:domain"))
}