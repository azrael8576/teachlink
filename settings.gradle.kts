pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
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
// teachlink
rootProject.name = "teachlink"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")

include(":core:analytics")
include(":core:network")
include(":core:data")
include(":core:common")
include(":core:model")
include(":core:domain")
include(":core:designsystem")
include(":core:testing")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:datastore-test")
include(":core:data-test")

include(":ui-test-hilt-manifest")

include(":feature:login")
include(":feature:teacherschedule")
include(":feature:contactme")
include(":feature:home")
