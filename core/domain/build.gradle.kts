plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.library.jacoco)
    alias(libs.plugins.convention.koin)
}

android {
    namespace = "eg.edu.cu.csds.icare.core.domain"
}
