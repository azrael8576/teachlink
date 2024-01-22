plugins {
    alias(libs.plugins.at.android.library)
    alias(libs.plugins.at.android.library.compose)
    alias(libs.plugins.at.android.hilt)
}

android {
    namespace = "com.wei.amazingtalker.core.designsystem"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    androidTestImplementation(projects.core.testing)

    // Write trace events to the system trace buffer.
    api(libs.androidx.tracing.ktx)
    // Material Design 3
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.windowSizeClass)
    api(libs.androidx.compose.material.iconsExtended)
    // main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.runtime)
    // Android Studio Preview support
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.tooling)
    // Optional - Integration with window
    api(libs.androidx.window)
    // Optional - Integration with LiveData
    api(libs.androidx.compose.runtime.livedata)
    // Optional - accompanist adaptive
    api(libs.accompanist.adaptive)

    implementation(libs.androidx.core.ktx)
    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    // Optional - Integration with browser
    implementation(libs.androidx.browser)
    // Coil
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)
    // kotlin datetime
    implementation(libs.kotlinx.datetime)
}
