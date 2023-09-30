import com.wei.amazingtalker_recruit.AtBuildType

plugins {
    id("at.android.application")
    id("at.android.application.compose")
    id("at.android.application.flavors")
    id("at.android.hilt")
}

android {
    namespace = "com.wei.amazingtalker_recruit"

    defaultConfig {
        applicationId = "com.wei.amazingtalker_recruit"
        /**
         * Version Code: AABCXYZ
         *
         * AA: API Level (33)
         *
         * BC: Supported screen sizes for this APK.
         * 12: Small to Normal screens
         * 34: Large to X-Large screens
         *
         * XYZ: App version (050 for 0.5.0)
         */
        versionCode = 3313050
        /**
         * SemVer major.minor.patch
         */
        versionName = "0.5.0"

        // Custom test runner to set up Hilt dependency graph
        testInstrumentationRunner = "com.wei.amazingtalker_recruit.core.testing.AtTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = AtBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true
            applicationIdSuffix = AtBuildType.RELEASE.applicationIdSuffix
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":feature:login"))
    implementation(project(":feature:teacherschedule"))
    implementation(project(":feature:contactme"))

    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))

    androidTestImplementation(project(":core:designsystem"))
    androidTestImplementation(project(":core:datastore-test"))
    androidTestImplementation(project(":core:testing"))
    testImplementation(project(":core:datastore-test"))
    testImplementation(project(":core:testing"))

    // PublicLibs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.tracing.ktx)
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    implementation(libs.androidx.activity.compose)

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

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    kspAndroidTest(libs.hilt.android.compiler)

    // Timber
    implementation(libs.timber)

    // Splashscreen
    implementation(libs.androidx.core.splashscreen)
}