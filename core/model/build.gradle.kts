plugins {
    id("at.android.library")
    id("at.android.hilt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.model"
}

dependencies {

    // PublicLibs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.tracing.ktx)
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // For androidx.compose.runtime.Stable
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.compose.ui.core)
}