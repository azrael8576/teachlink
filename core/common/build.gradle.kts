plugins {
    alias(libs.plugins.tl.android.library)
    alias(libs.plugins.tl.android.library.compose)
    alias(libs.plugins.tl.android.hilt)
}

android {
    namespace = "com.wei.teachlink.core.common"

    defaultConfig {
        testInstrumentationRunner = "com.wei.teachlink.core.testing.TlTestRunner"
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
