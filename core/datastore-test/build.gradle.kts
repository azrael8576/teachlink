plugins {
    id("at.android.library")
    id("at.android.hilt")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.datastore.test"
}

dependencies {
    api(project(":core:datastore"))
    implementation(project(":core:testing"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    // DataStore
    implementation(libs.androidx.datastore)

    // Protobuf
    implementation(libs.protobuf.kotlin.lite)
}