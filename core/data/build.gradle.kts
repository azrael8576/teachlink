plugins {
    alias(libs.plugins.tl.android.library)
    alias(libs.plugins.tl.android.hilt)
}

android {
    namespace = "com.wei.teachlink.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    api(projects.core.common)
    api(projects.core.network)
    api(projects.core.model)
    api(projects.core.datastore)
}
