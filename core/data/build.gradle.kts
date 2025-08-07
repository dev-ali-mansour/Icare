import dev.alimansour.shared.plugins.getSecret

plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.library.jacoco)
    alias(libs.plugins.convention.android.room)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "eg.edu.cu.csds.icare.core.data"

    defaultConfig {

        buildConfigField(
            "String",
            "WEB_CLIENT_ID",
            "\"${project.getSecret("WEB_CLIENT_ID")}\"",
        )
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            isMinifyEnabled = false
            enableUnitTestCoverage = false

            buildConfigField(
                "String",
                "BASE_URL",
                "\"${project.getSecret("RELEASE_BASE_URL")}\"",
            )
        }

        debug {
            isMinifyEnabled = false
            enableUnitTestCoverage = true

            buildConfigField(
                "String",
                "BASE_URL",
                "\"${project.getSecret("DEBUG_BASE_URL")}\"",
            )
        }
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
}

dependencies {

    implementation(projects.core.domain)

    implementation(libs.androidx.core.ktx)
    api(platform(libs.firebase.bom))
    api(libs.bundles.firebase)
    implementation(libs.bundles.retrofit)
    ksp(libs.koin.ksp.compiler)
}
