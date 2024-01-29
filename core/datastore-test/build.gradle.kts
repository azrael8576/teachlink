plugins {
    alias(libs.plugins.at.android.library)
    alias(libs.plugins.at.android.hilt)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.wei.amazingtalker.core.datastore.test"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(libs.hilt.android.testing)
    // DataStore
    implementation(libs.androidx.datastore)
    // Protobuf
    implementation(libs.protobuf.kotlin.lite)
}
