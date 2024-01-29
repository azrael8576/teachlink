plugins {
    alias(libs.plugins.at.android.library)
    alias(libs.plugins.at.android.library.compose)
    alias(libs.plugins.at.android.hilt)
}

android {
    namespace = "com.wei.amazingtalker.core.common"

    defaultConfig {
        testInstrumentationRunner = "com.wei.amazingtalker.core.testing.AtTestRunner"
    }
}

dependencies {
    // LifeCycle
    implementation(libs.androidx.lifecycle.runtimeCompose)
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(projects.core.testing)
    // For flow test
    testImplementation(libs.turbine)

    androidTestImplementation(projects.core.testing)
}
