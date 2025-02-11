import build.Build
import build.BuildConfig
import build.BuildDimensions
import build.BuildTypes
import extensions.getLocalProperty
import flavors.FlavorTypes
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import signing.SigningTypes
import test.TestBuildConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "eg.edu.cu.csds.icare.auth"
    compileSdk = BuildConfig.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = BuildConfig.MIN_SDK_VERSION

        testInstrumentationRunner = TestBuildConfig.TEST_INSTRUMENTATION_RUNNER
        consumerProguardFiles("consumer-rules.pro")
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
            signingConfig = signingConfigs.getByName(BuildTypes.RELEASE)
        }

        getByName(BuildTypes.DEBUG) {
            isMinifyEnabled = Build.Debug.isMinifyEnabled
            enableUnitTestCoverage = Build.Debug.enableUnitTestCoverage
            signingConfig = signingConfigs.getByName(BuildTypes.DEBUG)
        }

        create(BuildTypes.RELEASE_EXTERNAL_QA) {
            isMinifyEnabled = Build.ReleaseExternalQA.isMinifyEnabled
            enableUnitTestCoverage = Build.ReleaseExternalQA.enableUnitTestCoverage
            signingConfig = signingConfigs.getByName(BuildTypes.RELEASE_EXTERNAL_QA)
        }
    }
    flavorDimensions.add(BuildDimensions.APP)
    productFlavors {
        create(FlavorTypes.PATIENT) {
            dimension = BuildDimensions.APP
        }
        create(FlavorTypes.DOCTOR) {
            dimension = BuildDimensions.APP
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
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
tasks.getByPath("preBuild").dependsOn("ktlintFormat")
ktlint {
    android = true
    ignoreFailures = false
    outputToConsole = true
    outputColorName = "RED"
    verbose = true
    enableExperimentalRules = true
    filter {
        include("**/kotlin/**")
        exclude("**/generated/**")
    }
    reporters {
        reporter(ReporterType.JSON)
        reporter(ReporterType.CHECKSTYLE)
    }
}
dependencies {
    ksp(libs.koin.compiler)
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
}
