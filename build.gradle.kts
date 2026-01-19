import nl.littlerobots.vcu.plugin.resolver.VersionSelectors

subprojects {
    tasks {
        withType<Test>().configureEach {
            jvmArgs("-XX:+EnableDynamicAgentLoading")
        }
    }
}

buildscript {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
    dependencies {
        classpath(libs.google.oss.licenses.plugin) {
            exclude(group = "com.google.protobuf")
        }
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.dependency.guard) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.version.catalog.update) apply true
    alias(libs.plugins.module.graph) apply true
}

versionCatalogUpdate {
    sortByKey.set(false)
    versionSelector(VersionSelectors.STABLE)
    pin {
        versions.add("androidGradlePlugin")
    }
    keep {
        versions.add("jacoco")
    }
}
