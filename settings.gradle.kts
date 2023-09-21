pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "amazingtalker-recruit"
include(":app")
include(":core:network")
include(":core:data")
include(":core:common")
include(":core:model")
include(":core:domain")
include(":core:designsystem")

include(":feature:login")
include(":feature:teacherschedule")
include(":core:testing")
include(":core:datastore")
include(":feature:contactme")
