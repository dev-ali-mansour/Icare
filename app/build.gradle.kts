import build.Build
import build.BuildConfig
import build.BuildDimensions
import build.BuildTypes
import extensions.getLocalProperty
import extensions.osFamily
import flavors.FlavorTypes
import signing.SigningTypes
import test.TestBuildConfig

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
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
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = TestBuildConfig.TEST_INSTRUMENTATION_RUNNER
    }

    signingConfigs {
        create(SigningTypes.RELEASE) {
            storeFile = file(project.getLocalProperty("release_key.store.$osFamily"))
            storePassword = project.getLocalProperty("release_key.store_password")
            keyAlias = project.getLocalProperty("release_key.alias")
            keyPassword = project.getLocalProperty("release_key.key_password")
            enableV1Signing = true
            enableV2Signing = true
        }

        create(SigningTypes.RELEASE_EXTERNAL_QA) {
            storeFile = file(project.getLocalProperty("qa_key.store.$osFamily"))
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
                "proguard-kotlin-serialization.pro",
            )
            isMinifyEnabled = Build.Release.isMinifyEnabled
            enableUnitTestCoverage = Build.Release.enableUnitTestCoverage
            isDebuggable = Build.Release.isDebuggable
            signingConfig = signingConfigs.getByName(BuildTypes.RELEASE)
        }

        getByName(BuildTypes.DEBUG) {
            isMinifyEnabled = Build.Debug.isMinifyEnabled
            isDebuggable = Build.Debug.isDebuggable
            enableUnitTestCoverage = Build.Debug.enableUnitTestCoverage
            versionNameSuffix = Build.Debug.versionNameSuffix
            applicationIdSuffix = Build.Debug.applicationIdSuffix
            signingConfig = signingConfigs.getByName(BuildTypes.DEBUG)
        }

        create(BuildTypes.RELEASE_EXTERNAL_QA) {
            isMinifyEnabled = Build.ReleaseExternalQA.isMinifyEnabled
            isDebuggable = Build.ReleaseExternalQA.isDebuggable
            enableUnitTestCoverage = Build.ReleaseExternalQA.enableUnitTestCoverage
            versionNameSuffix = Build.ReleaseExternalQA.versionNameSuffix
            applicationIdSuffix = Build.ReleaseExternalQA.applicationIdSuffix
            signingConfig = signingConfigs.getByName(BuildTypes.RELEASE_EXTERNAL_QA)
        }
    }

    flavorDimensions.add(BuildDimensions.APP)
    productFlavors {
        create(FlavorTypes.PATIENT) {
            dimension = BuildDimensions.APP
            applicationIdSuffix = ".$name"
            versionNameSuffix = "-$name"
        }
        create(FlavorTypes.STAFF) {
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

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":features:on_boarding"))
    implementation(project(":features:auth"))
    implementation(project(":features:home"))
    implementation(project(":features:settings"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.multidex)
    implementation(libs.splashScreen)
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.timber)
    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.bundles.app.test)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.junit4)
}
