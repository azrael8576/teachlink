plugins {
    id("at.android.library")
    id("at.android.hilt")
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.common"

    defaultConfig {
        testInstrumentationRunner = "com.wei.amazingtalker_recruit.core.testing.AtTestRunner"
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    androidTestImplementation(project(":core:designsystem"))
    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))

    // PublicLibs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.tracing.ktx)
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // Okhttp Interceptor
    implementation(libs.okhttp.logging)

    // LifeCycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    // Navigation
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Timber
    implementation(libs.timber)
}