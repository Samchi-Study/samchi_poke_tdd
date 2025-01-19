pluginManagement {
    includeBuild("build-logic")
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

rootProject.name = "SamchiPokeTdd"
include(":app")
include(":feature-jungwon")
include(":feature-jinkwang")
include(":feature-kanghwi")
include(":feature-woosung")
include(":feature-sanghyung")
include(":network")
include(":model")
include(":feature-kanghwi:data")
include(":feature-kanghwi:presentation")
