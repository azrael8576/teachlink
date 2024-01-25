plugins {
    alias(libs.plugins.at.android.library)
    alias(libs.plugins.at.android.hilt)
}

android {
    namespace = "com.wei.amazingtalker.core.domain"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.network)
}
