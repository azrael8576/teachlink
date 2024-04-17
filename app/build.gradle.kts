import com.wei.teachlink.TlBuildType

plugins {
    alias(libs.plugins.tl.android.application)
    alias(libs.plugins.tl.android.application.compose)
    alias(libs.plugins.tl.android.application.flavors)
    alias(libs.plugins.tl.android.application.firebase)
    alias(libs.plugins.tl.android.hilt)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.wei.teachlink"

    defaultConfig {
        applicationId = "com.wei.teachlink"
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
        versionCode = 3414073
        /**
         * SemVer major.minor.patch
         */
        versionName = "0.7.3"

        // Custom test runner to set up Hilt dependency graph
        testInstrumentationRunner = "com.wei.teachlink.core.testing.TlTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = TlBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true
            applicationIdSuffix = TlBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

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
    implementation(projects.feature.contactme)
    implementation(projects.feature.home)
    implementation(projects.feature.login)
    implementation(projects.feature.teacherschedule)

    implementation(projects.core.analytics)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.datastore)
    implementation(projects.core.designsystem)
    implementation(projects.core.model)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    // Splashscreen
    implementation(libs.androidx.core.splashscreen)
    // Write trace events to the system trace buffer.
    implementation(libs.androidx.tracing.ktx)
    // LifeCycle
    implementation(libs.androidx.lifecycle.runtimeCompose)
    // WindowSizeClass
    implementation(libs.androidx.compose.material3.windowSizeClass)
    // Navigation
    implementation(libs.androidx.navigation.compose)
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    // Timber
    implementation(libs.timber)

    debugImplementation(projects.uiTestHiltManifest)
    // LeakCanary
    debugImplementation(libs.leakcanary)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    kspTest(libs.hilt.compiler)

    testImplementation(projects.core.datastoreTest)
    testImplementation(projects.core.testing)
    testImplementation(libs.accompanist.testharness)
    testImplementation(libs.hilt.android.testing)

    testDemoImplementation(libs.robolectric)
    testDemoImplementation(libs.roborazzi)

    androidTestImplementation(projects.core.datastoreTest)
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.accompanist.testharness)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.hilt.android.testing)
}
