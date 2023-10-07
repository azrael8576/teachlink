plugins {
    alias(libs.plugins.at.android.library)
    alias(libs.plugins.at.android.library.compose)
    alias(libs.plugins.at.android.hilt)
}

android {
    namespace = "com.wei.amazingtalker.core.testing"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))

    api(libs.junit4)
    api(libs.androidx.compose.ui.test)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.core)
    api(libs.androidx.test.runner)
    // Coroutines test
    api(libs.kotlinx.coroutines.test)
    api(libs.hilt.android.testing)
    // Google truth
    api(libs.google.truth)
    // For flow test
    api(libs.turbine)

    debugApi(libs.androidx.compose.ui.testManifest)
}