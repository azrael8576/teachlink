plugins {
    alias(libs.plugins.tl.android.feature)
    alias(libs.plugins.tl.android.library.compose)
    alias(libs.plugins.tl.android.hilt)
}

android {
    namespace = "com.wei.teachlink.feature.login"
}

dependencies {
    implementation(projects.core.data)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}
