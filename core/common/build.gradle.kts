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
    implementation(projects.core.model)

    // LifeCycle
    implementation(libs.androidx.lifecycle.runtimeCompose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
}
