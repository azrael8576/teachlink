plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.secrets)
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
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

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

secrets {
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    testImplementation(project(":core:testing"))

    // PublicLibs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.tracing.ktx)
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // Okhttp Interceptor
    implementation(libs.okhttp.logging)

    // Retrofit2
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)

    // RetrofitKotlinxSerializationJson
    implementation(libs.retrofit.kotlin.serialization)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // KotlinxDatetime
    implementation(libs.kotlinx.datetime)

    // KotlinxSerializationJson
    implementation(libs.kotlinx.serialization.json)
}