plugins {
    alias(libs.plugins.at.android.library)
    alias(libs.plugins.at.android.hilt)
    alias(libs.plugins.secrets)
    id("kotlinx-serialization")
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