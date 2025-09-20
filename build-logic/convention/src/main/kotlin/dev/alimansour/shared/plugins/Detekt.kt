package dev.alimansour.shared.plugins

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

internal fun Project.configureDetekt() {
    pluginManager.apply(findPlugin("detekt"))

    configure<DetektExtension> {
        source.setFrom("src/main/java", "src/main/kotlin")
        ignoredBuildTypes = listOf("release")
    }

    val reportMerge =
        tasks.register<ReportMergeTask>("detektReportMerge") {
            output.set(layout.buildDirectory.file("reports/detekt/merge.xml"))
            input.from(tasks.withType<Detekt>().map { it.xmlReportFile }) // or .sarifReportFile
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
                        .set(
                            file(
                                "${rootProject.projectDir}/detekt/reports/${project.name}/detekt-report.xml",
                            ),
                        )
                }
                html {
                    required.set(false)
                    outputLocation
                        .set(
                            file(
                                "${rootProject.projectDir}/detekt/reports/${project.name}/detekt-report.html",
                            ),
                        )
                }
                sarif {
                    required.set(false)
                    outputLocation
                        .set(
                            file(
                                "${rootProject.projectDir}/detekt/reports/${project.name}/detekt-report.sarif",
                            ),
                        )
                }
                md {
                    required.set(false)
                    outputLocation
                        .set(
                            file(
                                "${rootProject.projectDir}/detekt/reports/${project.name}/detekt-report.md",
                            ),
                        )
                }
                txt {
                    required.set(false)
                    outputLocation
                        .set(
                            file(
                                "${rootProject.projectDir}/detekt/reports/${project.name}/detekt-report.txt",
                            ),
                        )
                }
            }
            jvmTarget = JavaVersion.VERSION_21.toString()
            parallel = false
            config.from("${rootProject.projectDir}/config/detekt/detekt.yml")
            buildUponDefaultConfig = false
            allRules = false
            disableDefaultRuleSets = false
            debug = true
            ignoreFailures = false
            basePath = projectDir.absolutePath
        }.configureEach {
            finalizedBy(reportMerge)
        }

        getByPath("preBuild")
            .dependsOn(":versionCatalogUpdate")
            .dependsOn("ktlintFormat")
            .dependsOn("detekt")

        getByName<Delete>("clean") {
            delete.addAll(
                listOf(
                    "${rootProject.projectDir}/build/reports/detekt",
                    "${rootProject.projectDir}/detekt/reports",
                ),
            )
        }
    }
}
