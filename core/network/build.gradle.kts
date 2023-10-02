plugins {
    id("at.android.library")
    id("at.android.hilt")
    id("kotlinx-serialization")
    alias(libs.plugins.secrets)
}

android {
    namespace = "com.wei.amazingtalker.core.network"

    buildFeatures {
        buildConfig = true
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
    // Okhttp Interceptor
    implementation(libs.okhttp.logging)

    // Retrofit2
    implementation(libs.retrofit.core)

    // RetrofitKotlinxSerializationJson
    implementation(libs.retrofit.kotlin.serialization)

    // KotlinxSerializationJson
    implementation(libs.kotlinx.serialization.json)
}