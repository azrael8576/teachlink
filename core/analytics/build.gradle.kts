plugins {
    alias(libs.plugins.tl.android.library)
    alias(libs.plugins.tl.android.library.compose)
    alias(libs.plugins.tl.android.hilt)
}

android {
    namespace = "com.wei.teachlink.core.analytics"
}

dependencies {
    implementation(libs.androidx.compose.runtime)

    prodImplementation(platform(libs.firebase.bom))
    prodImplementation(libs.firebase.analytics)
}
