plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.network"
    compileSdk = 33

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val AmazingTalkerServerUrl: String by project
        getByName("debug") {
            buildConfigField("String", "ServerUrl", AmazingTalkerServerUrl)
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
            buildConfigField("String", "ServerUrl", AmazingTalkerServerUrl)
        }
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {

    // PublicLibs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.tracing.ktx)
    coreLibraryDesugaring(libs.android.desugarJdkLibs)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
//    androidTestImplementation(libs.androidx.test.espresso.core)

    // Okhttp Interceptor
    implementation(libs.okhttp.logging)

    // Retrofit2
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)

    // RetrofitKotlinxSerializationJson
    implementation(libs.retrofit.kotlin.serialization)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // KotlinxDatetime
    implementation(libs.kotlinx.datetime)

    // KotlinxSerializationJson
    implementation(libs.kotlinx.serialization.json)
}