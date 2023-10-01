plugins {
    id("at.android.library")
    id("at.android.hilt")
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
}