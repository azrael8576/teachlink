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
rootProject.name = "amazingtalker"
include(":app")

include(":core:network")
include(":core:data")
include(":core:common")
include(":core:model")
include(":core:domain")
include(":core:designsystem")
include(":core:testing")
include(":core:datastore")
include(":core:datastore-test")

include(":feature:login")
include(":feature:teacherschedule")
include(":feature:contactme")
