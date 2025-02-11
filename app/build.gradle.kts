
import build.Build
import build.BuildConfig
import build.BuildDimensions
import build.BuildTypes
import build.BuildVariables
import extensions.getLocalProperty
import flavors.FlavorTypes
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import release.ReleaseConfig
import signing.SigningTypes
import test.TestBuildConfig

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

android {
    namespace = BuildConfig.APP_ID
    compileSdk = BuildConfig.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = BuildConfig.APP_ID
        minSdk = BuildConfig.MIN_SDK_VERSION
        targetSdk = BuildConfig.TARGET_SDK_VERSION
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        versionCode = ReleaseConfig.VERSION_CODE
        versionName = ReleaseConfig.VERSION_NAME

        testInstrumentationRunner = TestBuildConfig.TEST_INSTRUMENTATION_RUNNER
    }
    signingConfigs {
        create(SigningTypes.RELEASE) {
            storeFile = file(project.getLocalProperty("release_key.store"))
            storePassword = project.getLocalProperty("release_key.store_password")
            keyAlias = project.getLocalProperty("release_key.alias")
            keyPassword = project.getLocalProperty("release_key.key_password")
            enableV1Signing = true
            enableV2Signing = true
        }
        create(SigningTypes.RELEASE_EXTERNAL_QA) {
            storeFile = file(project.getLocalProperty("qa_key.store"))
            storePassword = project.getLocalProperty("qa_key.store_password")
            keyAlias = project.getLocalProperty("qa_key.alias")
            keyPassword = project.getLocalProperty("qa_key.key_password")
            enableV1Signing = true
            enableV2Signing = true
        }
        getByName(SigningTypes.DEBUG) {
            storeFile = File(project.rootProject.rootDir, "debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            enableV1Signing = true
            enableV2Signing = true
        }
    }
    buildTypes {
        getByName(BuildTypes.RELEASE) {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            isMinifyEnabled = Build.Release.isMinifyEnabled
            enableUnitTestCoverage = Build.Release.enableUnitTestCoverage
            isDebuggable = Build.Release.isDebuggable
            signingConfig = signingConfigs.getByName(BuildTypes.RELEASE)

            buildConfigField(
                "String",
                BuildVariables.BASE_URL,
                project.getLocalProperty("prod_endpoint"),
            )
            buildConfigField(
                "boolean",
                BuildVariables.CAN_CLEAR_CACHE,
                project.getLocalProperty("dev.clear_cache"),
            )
            buildConfigField(
                "int",
                BuildVariables.DB_VERSION,
                project.getLocalProperty("dev.db_version"),
            )
        }

        getByName(BuildTypes.DEBUG) {
            isMinifyEnabled = Build.Debug.isMinifyEnabled
            isDebuggable = Build.Debug.isDebuggable
            enableUnitTestCoverage = Build.Debug.enableUnitTestCoverage
            versionNameSuffix = Build.Debug.versionNameSuffix
            applicationIdSuffix = Build.Debug.applicationIdSuffix
            signingConfig = signingConfigs.getByName(BuildTypes.DEBUG)

            buildConfigField(
                "String",
                BuildVariables.BASE_URL,
                project.getLocalProperty("debug_endpoint"),
            )
            buildConfigField(
                "boolean",
                BuildVariables.CAN_CLEAR_CACHE,
                project.getLocalProperty("dev.clear_cache"),
            )
            buildConfigField(
                "int",
                BuildVariables.DB_VERSION,
                project.getLocalProperty("dev.db_version"),
            )
        }

        create(BuildTypes.RELEASE_EXTERNAL_QA) {
            isMinifyEnabled = Build.ReleaseExternalQA.isMinifyEnabled
            isDebuggable = Build.ReleaseExternalQA.isDebuggable
            enableUnitTestCoverage = Build.ReleaseExternalQA.enableUnitTestCoverage
            versionNameSuffix = Build.ReleaseExternalQA.versionNameSuffix
            applicationIdSuffix = Build.ReleaseExternalQA.applicationIdSuffix
            signingConfig = signingConfigs.getByName(BuildTypes.RELEASE_EXTERNAL_QA)

            buildConfigField(
                "String",
                BuildVariables.BASE_URL,
                project.getLocalProperty("qa_endpoint"),
            )
            buildConfigField(
                "boolean",
                BuildVariables.CAN_CLEAR_CACHE,
                project.getLocalProperty("debug_endpoint"),
            )
            buildConfigField(
                "int",
                BuildVariables.DB_VERSION,
                project.getLocalProperty("dev.clear_cache"),
            )
        }
    }
    flavorDimensions.add(BuildDimensions.APP)
    productFlavors {

        create(FlavorTypes.PATIENT) {
            dimension = BuildDimensions.APP
            applicationIdSuffix = ".$name"
            versionNameSuffix = "-$name"
        }
        create(FlavorTypes.DOCTOR) {
            dimension = BuildDimensions.APP
            applicationIdSuffix = ".$name"
            versionNameSuffix = "-$name"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeCompiler {
        featureFlags.set(setOf(ComposeFeatureFlag.StrongSkipping.disabled()))
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

detekt {
    source.setFrom("src/main/java", "src/main/kotlin")
    ignoredBuildTypes = listOf("release")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.multidex)
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
    implementation(libs.splashScreen)
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.timber)

    testImplementation(libs.junit)

    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
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
