import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false
}
val reportMerge by tasks.registering(io.gitlab.arturbosch.detekt.report.ReportMergeTask::class) {
    output = project.layout.buildDirectory.file("reports/detekt/merge.xml")
}

subprojects {
    apply<DetektPlugin>()
    apply<KtlintPlugin>()

    configure<KtlintExtension> {
        android.set(true)
        ignoreFailures = false
        outputToConsole = true
        outputColorName = "RED"
        verbose = true
        enableExperimentalRules = true
        filter {
            include("**/kotlin/**")
            exclude("**/generated/**")
        }
        reporters {
            reporter(ReporterType.JSON)
            reporter(ReporterType.CHECKSTYLE)
        }
    }

    tasks {
        withType<Detekt> {
            include("**/*.kt", "**/*.kts")
            exclude(
                "**/build/**",
                ".*/resources/.*",
                ".*test.*",
                ".*/tmp/.*",
                "**/generated/**",
            )
            reports {
                xml {
                    required.set(true)
                    outputLocation
                        .set(file("${rootProject.projectDir}/detekt/reports/${project.name}/detekt-report.xml"))
                }
                html {
                    required.set(false)
                    outputLocation
                        .set(file("${rootProject.projectDir}/detekt/reports/${project.name}/detekt-report.html"))
                }
                sarif {
                    required.set(false)
                    outputLocation
                        .set(file("${rootProject.projectDir}/detekt/reports/${project.name}/detekt-report.sarif"))
                }
                md {
                    required.set(false)
                    outputLocation
                        .set(file("${rootProject.projectDir}/detekt/reports/${project.name}/detekt-report.md"))
                }
                txt {
                    required.set(false)
                    outputLocation
                        .set(file("${rootProject.projectDir}/detekt/reports/${project.name}/detekt-report.txt"))
                }
            }
            jvmTarget = JavaVersion.VERSION_21.toString()

//            source.setFrom("src/main/java", "src/main/kotlin")
            parallel = false
            config.from("${rootProject.projectDir}/config/detekt/detekt.yml")
            buildUponDefaultConfig = false
            allRules = false
            disableDefaultRuleSets = false
            debug = true
            ignoreFailures = false
//            ignoredBuildTypes = listOf("release")
            basePath = projectDir.absolutePath
        }.configureEach {
            finalizedBy(reportMerge)
        }
    }

    reportMerge {
        input.from(tasks.withType<Detekt>().map { it.xmlReportFile }) // or .sarifReportFile
    }
}
