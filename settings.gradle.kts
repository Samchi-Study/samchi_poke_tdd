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
include(":network")
include(":model")
include(":common")
include(":feature-jungwon")
include(":feature-jinkwang")
include(":feature-kanghwi")
include(":feature-woosung")
include(":feature-sanghyeong")
include(":feature-kanghwi")
include(":feature-kanghwi:data")
include(":feature-kanghwi:presentation")
include(":feature-kanghwi:db")
include(":feature-kanghwi:model")
