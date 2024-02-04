plugins {
    alias(libs.plugins.tl.android.library)
    alias(libs.plugins.tl.android.library.compose)
    alias(libs.plugins.tl.android.hilt)
    id("kotlin-parcelize")
}

android {
    namespace = "com.wei.teachlink.core.model"
}

dependencies {
    // For androidx.compose.runtime.Stable
    implementation(libs.androidx.compose.runtime)
}