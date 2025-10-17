rootProject.name = "NBP-Rates"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":core:ui")
include(":core:network")
include(":core:database")
include(":feature:currency-list:currency-list-di")
include(":feature:currency-list:currency-list-data")
include(":feature:currency-list:currency-list-domain")
include(":feature:currency-list:currency-list-network")
include(":feature:currency-list:currency-list-presentation")

include(":feature:currency-details:currency-details-data")
include(":feature:currency-details:currency-details-domain")
include(":feature:currency-details:currency-details-network")
include(":feature:currency-details:currency-details-presentation")
