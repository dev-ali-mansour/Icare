import build.Build
import build.BuildConfig
import build.BuildDimensions
import build.BuildTypes
import extensions.getSecret
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
    namespace = "${BuildConfig.APP_ID}.core.ui"
    compileSdk = BuildConfig.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = BuildConfig.MIN_SDK_VERSION

        testInstrumentationRunner = TestBuildConfig.TEST_INSTRUMENTATION_RUNNER
        consumerProguardFiles("consumer-rules.pro")
    }

    signingConfigs {
        create(SigningTypes.RELEASE) {
            val keystoreFile = rootProject.file("release-key.jks")
            storeFile = keystoreFile
            storePassword = project.getSecret("KEYSTORE_PASSWORD")
            keyAlias = project.getSecret("KEY_ALIAS")
            keyPassword = project.getSecret("KEY_PASSWORD")
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

    buildFeatures {
        compose = true
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
    arg("KOIN_DEFAULT_MODULE", "true")
    arg("KOIN_CONFIG_CHECK", "true")
}

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(project(":core:domain"))
    api(project(":data"))

    api(libs.timber)
    api(platform(libs.androidx.compose.bom))
    api(libs.bundles.compose)
    api(libs.bundles.coil)
    api(libs.bundles.appcompat)
    api(platform(libs.koin.bom))
    api(libs.bundles.koin.android)
    api(platform(libs.firebase.bom))
    api(libs.firebase.auth)
    implementation(libs.androidx.browser)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.bundles.app.test)
    androidTestImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.junit4)
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_21.majorVersion.toInt())
}
