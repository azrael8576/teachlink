plugins {
    id("at.android.library")
    id("at.android.hilt")
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
}