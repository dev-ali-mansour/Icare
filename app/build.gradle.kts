import dev.alimansour.shared.plugins.BuildType
import test.TestBuildConfig

plugins {
    alias(libs.plugins.convention.android.application)
    alias(libs.plugins.convention.android.application.compose)
    alias(libs.plugins.convention.android.application.flavors)
    alias(libs.plugins.convention.android.application.jacoco)
    alias(libs.plugins.convention.android.application.firebase)
    alias(libs.plugins.convention.koin)
}

android {

    defaultConfig {
        applicationId = "eg.edu.cu.csds.icare"
        vectorDrawables.useSupportLibrary = true
        versionCode = project.findProperty("VERSION_CODE")?.toString()?.toInt() ?: 1
        versionName = project.findProperty("VERSION_NAME")?.toString() ?: "1.0.0"

        testInstrumentationRunner = TestBuildConfig.TEST_INSTRUMENTATION_RUNNER
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            versionNameSuffix = BuildType.DEBUG.versionNameSuffix
            applicationIdSuffix = BuildType.DEBUG.applicationIdSuffix
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "proguard-kotlin-serialization.pro",
            )
            isMinifyEnabled = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "eg.edu.cu.csds.icare"
}

dependencies {
    implementation(projects.features.onBoarding)
    implementation(projects.features.auth)
    implementation(projects.features.home)
    implementation(projects.features.notifications)
    implementation(projects.features.settings)

    implementation(projects.core.ui)

    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.multidex)
    implementation(libs.splashScreen)
    implementation(libs.app.update)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.timber)
    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.bundles.app.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.ui.test.junit4)
}

dependencyGuard {
    configuration("patientReleaseRuntimeClasspath")
    configuration("staffReleaseRuntimeClasspath")
}
