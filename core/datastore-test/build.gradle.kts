plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.datastore.test"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    api(project(":core:datastore"))
    implementation(project(":core:testing"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    // PublicLibs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.tracing.ktx)
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // DataStore
    implementation(libs.androidx.datastore)

    // Protobuf
    implementation(libs.protobuf.kotlin.lite)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

}