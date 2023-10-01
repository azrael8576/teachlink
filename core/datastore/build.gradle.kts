plugins {
    id("at.android.library")
    id("at.android.hilt")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.wei.amazingtalker_recruit.core.datastore"

    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

// Setup protobuf configuration, generating lite Java and Kotlin classes
protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

androidComponents.beforeVariants {
    android.sourceSets.register(it.name) {
        java.srcDir(buildDir.resolve("generated/source/proto/${it.name}/java"))
        kotlin.srcDir(buildDir.resolve("generated/source/proto/${it.name}/kotlin"))
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    // DataStore
    implementation(libs.androidx.datastore)

    // Protobuf
    implementation(libs.protobuf.kotlin.lite)
}