plugins {
    alias(libs.plugins.tl.android.feature)
    alias(libs.plugins.tl.android.library.compose)
    alias(libs.plugins.tl.android.hilt)
}

android {
    namespace = "com.wei.teachlink.feature.contactme"
}

dependencies {
    implementation(projects.core.data)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}
