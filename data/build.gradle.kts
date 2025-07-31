import build.Build
import build.BuildConfig
import build.BuildDimensions
import build.BuildTypes
import build.BuildVariables
import extensions.getSecret
import flavors.FlavorTypes
import signing.SigningTypes
import test.TestBuildConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

android {
    namespace = "${BuildConfig.APP_ID}.data"
    compileSdk = BuildConfig.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = BuildConfig.MIN_SDK_VERSION

        testInstrumentationRunner = TestBuildConfig.TEST_INSTRUMENTATION_RUNNER
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            BuildVariables.WEB_CLIENT_ID,
            project.getSecret("WEB_CLIENT_ID"),
        )
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

            buildConfigField(
                "String",
                BuildVariables.BASE_URL,
                project.getSecret("RELEASE_BASE_URL"),
            )
        }

        getByName(BuildTypes.DEBUG) {
            isMinifyEnabled = Build.Debug.isLibraryMinifyEnabled
            enableUnitTestCoverage = Build.Debug.enableUnitTestCoverage
            signingConfig = signingConfigs.getByName(BuildTypes.DEBUG)

            buildConfigField(
                "String",
                BuildVariables.BASE_URL,
                project.getSecret("DEBUG_BASE_URL"),
            )
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
        buildConfig = true
    }

    testOptions.unitTests {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }

    sourceSets {
        getByName("test") {
            resources.srcDir("src/test/resources")
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
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

    implementation(projects.core.domain)

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.koin.bom))
    api(libs.timber)
    api(platform(libs.firebase.bom))
    api(libs.bundles.firebase)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.room)
    ksp(libs.koin.ksp.compiler)
    ksp(libs.room.compiler)

    testImplementation(libs.bundles.data.test)
    testImplementation(libs.room.testing)
    androidTestImplementation(libs.androidx.junit)
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_21.majorVersion.toInt())
}
