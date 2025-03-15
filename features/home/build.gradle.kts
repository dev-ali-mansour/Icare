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
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

android {
    namespace = "${BuildConfig.APP_ID}.home"
    compileSdk = BuildConfig.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = BuildConfig.MIN_SDK_VERSION

        testInstrumentationRunner = TestBuildConfig.TEST_INSTRUMENTATION_RUNNER
        consumerProguardFiles("consumer-rules.pro")
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
            )
            isMinifyEnabled = Build.Release.isLibraryMinifyEnabled
            enableUnitTestCoverage = Build.Release.enableUnitTestCoverage
            signingConfig = signingConfigs.getByName(BuildTypes.RELEASE)
        }

        getByName(BuildTypes.DEBUG) {
            isMinifyEnabled = Build.Debug.isLibraryMinifyEnabled
            enableUnitTestCoverage = Build.Debug.enableUnitTestCoverage
            signingConfig = signingConfigs.getByName(BuildTypes.DEBUG)
        }

        create(BuildTypes.RELEASE_EXTERNAL_QA) {
            isMinifyEnabled = Build.ReleaseExternalQA.isLibraryMinifyEnabled
            enableUnitTestCoverage = Build.ReleaseExternalQA.enableUnitTestCoverage
            signingConfig = signingConfigs.getByName(BuildTypes.RELEASE_EXTERNAL_QA)
        }
    }

    flavorDimensions.add(BuildDimensions.APP)
    productFlavors {
        create(FlavorTypes.PATIENT) {
            dimension = BuildDimensions.APP
        }
        create(FlavorTypes.STAFF) {
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

tasks {
    getByPath("preBuild").dependsOn("ktlintFormat").dependsOn("detekt")
}

detekt {
    source.setFrom("src/main/java", "src/main/kotlin")
    ignoredBuildTypes = listOf("release")
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}

dependencies {
    implementation(project(":core:ui"))

    ksp(libs.koin.ksp.compiler)
    testImplementation(libs.bundles.domain.test)
    androidTestImplementation(libs.bundles.app.test)
}
