package dev.alimansour.shared.plugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

internal fun Project.configureKtlint() {
    pluginManager.apply(findPlugin("ktlint"))

    configure<KtlintExtension> {
        android.set(true)
        ignoreFailures.set(false)
        outputToConsole.set(true)
        outputColorName.set("RED")
        verbose.set(true)
        enableExperimentalRules.set(true)

        filter {
            include("**/kotlin/**")
            exclude("**/generated/**")
        }
        reporters {
            reporter(ReporterType.JSON)
            reporter(ReporterType.CHECKSTYLE)
        }
    }
}
