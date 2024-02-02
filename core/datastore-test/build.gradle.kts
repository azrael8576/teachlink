plugins {
    alias(libs.plugins.tl.android.library)
    alias(libs.plugins.tl.android.hilt)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.wei.teachlink.core.datastore.test"
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
