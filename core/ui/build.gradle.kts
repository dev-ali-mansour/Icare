plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.library.flavors)
    alias(libs.plugins.convention.android.library.compose)
    alias(libs.plugins.convention.android.library.jacoco)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "eg.edu.cu.csds.icare.core.ui"
}

dependencies {
    api(projects.core.domain)
    api(projects.core.data)

    implementation(libs.androidx.browser)
}
