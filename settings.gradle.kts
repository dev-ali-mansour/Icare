pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Icare"
include(":app")
include(":core:domain")
include(":core:ui")
include(":data")
include(":features:on_boarding")
include(":features:auth")
include(":features:home")
include(":features:settings")
include(":features:admin")
include(":features:appointments")
include(":features:consultations")
