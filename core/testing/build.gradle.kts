plugins {
    alias(libs.plugins.at.android.library)
    alias(libs.plugins.at.android.library.compose)
    alias(libs.plugins.at.android.hilt)
}

android {
    namespace = "com.wei.amazingtalker.core.testing"
}

dependencies {
    api(kotlin("test"))
    api(libs.androidx.compose.ui.test)
    // Google truth
    api(libs.google.truth)
    // For screenshot tests
    api(libs.roborazzi)
    api(projects.core.data)
    api(projects.core.model)

    debugApi(libs.androidx.compose.ui.testManifest)

    // testharness
    implementation(libs.accompanist.testharness)
    implementation(libs.androidx.activity.compose)
    implementation(libs.hilt.android.testing)
    // Coroutines test
    implementation(libs.kotlinx.coroutines.test)
    // Android Unit Testing Framework
    implementation(libs.robolectric.shadows)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
}
