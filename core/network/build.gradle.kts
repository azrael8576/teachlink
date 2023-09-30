plugins {
    id("at.android.library")
    id("at.android.hilt")
    id("kotlinx-serialization")
    alias(libs.plugins.secrets)
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.network"

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