plugins {
    alias(libs.plugins.at.android.library)
    alias(libs.plugins.at.android.hilt)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.wei.amazingtalker.core.datastore.test"
}

dependencies {
    api(projects.core.datastore)
    implementation(projects.core.testing)
    implementation(projects.core.common)
    implementation(projects.core.model)

    // DataStore
    implementation(libs.androidx.datastore)

    // Protobuf
    implementation(libs.protobuf.kotlin.lite)
}
