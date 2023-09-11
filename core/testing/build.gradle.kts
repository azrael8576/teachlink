plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.testing"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
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

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)
    kaptAndroidTest(libs.hilt.android.compiler)

    // Timber
    implementation(libs.timber)
}