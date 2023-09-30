plugins {
    id("at.android.library")
    id("at.android.library.compose")
    id("at.android.hilt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.testing"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))

    api(libs.junit4)
    api(libs.androidx.arch.core.test)
    api(libs.androidx.compose.ui.test)
    api(libs.androidx.compose.ui.ui.test.junit4)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.core)
    api(libs.androidx.test.ext)
    api(libs.androidx.test.runner)
    api(libs.kotlinx.coroutines.test)
    api(libs.hilt.android.test)
    api(libs.google.truth)
    api(libs.okhttp.mockwebserver)

    // For flow test
    api(libs.app.cash.turbine)
    api(kotlin("test"))

    debugApi(libs.androidx.compose.ui.testManifest)

    // PublicLibs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.tracing.ktx)
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Timber
    implementation(libs.timber)
}