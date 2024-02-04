plugins {
    alias(libs.plugins.tl.android.library)
    alias(libs.plugins.tl.android.hilt)
}

android {
    namespace = "com.wei.teachlink.core.datastore"

    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    api(projects.core.datastoreProto)
    api(projects.core.model)

    implementation(projects.core.common)
    // DataStore
    implementation(libs.androidx.datastore)
    // Protobuf
    implementation(libs.protobuf.kotlin.lite)
}
