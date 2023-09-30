plugins {
    id("at.android.library")
    id("at.android.library.compose")
    id("at.android.hilt")
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.designsystem"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    androidTestImplementation(project(":core:testing"))

    // PublicLibs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.tracing.ktx)
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Timber
    implementation(libs.timber)

    // Compose
    api(platform(libs.androidx.compose.bom))

    // Material Design 3
    api(libs.androidx.compose.material3.core)
    api(libs.androidx.compose.material3.windowSizeClass)
    api(libs.androidx.compose.material.iconsExtended)

    // main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    api(libs.androidx.compose.ui.core)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.foundation)

    // Integration with Navigation and Hilt
    api(libs.androidx.hilt.navigation.compose)

    // Android Studio Preview support
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.ui.tooling)

    // Optional - Integration with activities
    api(libs.androidx.activity.compose)

    // Optional - Integration with window
    api(libs.androidx.window)

    // Optional - Integration with LiveData
    api(libs.androidx.compose.runtime.livedata)

    // Optional - accompanist adaptive
    api(libs.accompanist.adaptive)

    // UI Tests
    api(libs.androidx.compose.ui.ui.test.junit4)

    // Coil
    api(libs.coil.kt)
    api(libs.coil.kt.compose)
    api(libs.coil.kt.svg)
}