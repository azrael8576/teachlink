plugins {
    alias(libs.plugins.at.android.feature)
    alias(libs.plugins.at.android.library.compose)
    alias(libs.plugins.at.android.hilt)
}

android {
    namespace = "com.wei.amazingtalker.feature.contactme"
}

dependencies {
    implementation(projects.core.data)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}
