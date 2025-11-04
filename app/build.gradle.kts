import dev.alimansour.shared.plugins.BuildType

plugins {
    alias(libs.plugins.convention.android.application)
    alias(libs.plugins.convention.android.application.compose)
    alias(libs.plugins.convention.android.application.flavors)
    alias(libs.plugins.convention.android.application.jacoco)
    alias(libs.plugins.convention.android.application.firebase)
    alias(libs.plugins.convention.koin)
}

val dynamicVersionCode: Int? = System.getenv("VERSION_CODE")?.toIntOrNull()
val dynamicVersionName: String? = System.getenv("VERSION_NAME")

android {

    defaultConfig {
        applicationId = "eg.edu.cu.csds.icare"
        vectorDrawables.useSupportLibrary = true
        versionCode = dynamicVersionCode ?: 3
        versionName = dynamicVersionName ?: "1.0.2"
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
    implementation(projects.feature.onBoarding)
    implementation(projects.feature.auth)
    implementation(projects.feature.home)
    implementation(projects.feature.notifications)
    implementation(projects.feature.settings)

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.ui)

    testImplementation(libs.bundles.app.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.ui.test.junit4)
}

dependencyGuard {
    configuration("patientReleaseRuntimeClasspath")
    configuration("staffReleaseRuntimeClasspath")
}
