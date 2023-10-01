plugins {
    id("at.android.library")
    id("at.android.library.compose")
    id("at.android.hilt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.model"
}

dependencies {
    // For androidx.compose.runtime.Stable
    implementation(libs.androidx.compose.runtime)
}