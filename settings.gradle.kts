rootProject.name = "DroidPasswords"
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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")

include(":core")
include(":data")

include(":features:edit")
include(":features:export")
include(":features:importdata")
include(":features:itemdetails")
include(":features:itemlist")
include(":features:setlock")
include(":features:settings")
include(":features:tags")
include(":features:unlock")
include(":features:welcome")

include(":features-api:edit")
include(":features-api:export")
include(":features-api:importdata")
include(":features-api:itemdetails")
include(":features-api:itemlist")
include(":features-api:setlock")
include(":features-api:settings")
include(":features-api:tags")
include(":features-api:unlock")
include(":features-api:welcome")
