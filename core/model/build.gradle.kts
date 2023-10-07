plugins {
    alias(libs.plugins.at.android.library)
    alias(libs.plugins.at.android.library.compose)
    alias(libs.plugins.at.android.hilt)
    id("kotlin-parcelize")
}

android {
    namespace = "com.wei.amazingtalker.core.model"
}

dependencies {
    // For androidx.compose.runtime.Stable
    implementation(libs.androidx.compose.runtime)
}