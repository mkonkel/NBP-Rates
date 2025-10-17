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
include(":feature:currency_list:currency-list-data")
include(":feature:currency_list:currency-list-domain")
include(":feature:currency_list:currency-list-network")
include(":feature:currency_list:currency-list-presentation")
include(":feature:currency_details:currency_details-data")
include(":feature:currency_details:currency_details-domain")
include(":feature:currency_details:currency_details-network")
include(":feature:currency_details:currency_details-presentation")
