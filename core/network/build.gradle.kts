plugins {
    alias(libs.plugins.tl.android.library)
    alias(libs.plugins.tl.android.hilt)
    alias(libs.plugins.secrets)
    id("kotlinx-serialization")
}

android {
    namespace = "com.wei.teachlink.core.network"

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
    api(projects.core.common)
    api(projects.core.model)

    // KotlinxSerializationJson
    implementation(libs.kotlinx.serialization.json)
    // Okhttp Interceptor
    implementation(libs.okhttp.logging)
    // Retrofit2
    implementation(libs.retrofit.core)
    // RetrofitKotlinxSerializationJson
    implementation(libs.retrofit.kotlin.serialization)
}
