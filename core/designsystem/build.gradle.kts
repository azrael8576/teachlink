plugins {
    alias(libs.plugins.tl.android.library)
    alias(libs.plugins.tl.android.library.compose)
    alias(libs.plugins.tl.android.hilt)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.wei.teachlink.core.designsystem"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    androidTestImplementation(projects.core.testing)

    // Material Design 3
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.iconsExtended)
    // main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.runtime)
    // Android Studio Preview support
    api(libs.androidx.compose.ui.tooling.preview)
    // Optional - Integration with window
    api(libs.androidx.window)
    // Optional - accompanist adaptive
    api(libs.accompanist.adaptive)

    debugApi(libs.androidx.compose.ui.tooling)

    // Coil
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)

    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.accompanist.testharness)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(projects.core.testing)

    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
