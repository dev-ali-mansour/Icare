plugins {
    alias(libs.plugins.convention.android.feature)
    alias(libs.plugins.convention.android.library.compose)
    alias(libs.plugins.convention.android.library.jacoco)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "eg.edu.cu.csds.icare.admin"
}
