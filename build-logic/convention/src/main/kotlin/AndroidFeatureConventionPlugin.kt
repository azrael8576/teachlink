import com.android.build.gradle.LibraryExtension
import com.wei.amazingtalker_recruit.configureGradleManagedDevices
import com.wei.amazingtalker_recruit.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("at.android.library")
                apply("at.android.hilt")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner =
                        "com.wei.amazingtalker_recruit.core.testing.AtTestRunner"
                }
                configureGradleManagedDevices(this)
            }

            dependencies {
                add("implementation", project(":core:network"))
                add("implementation", project(":core:common"))
                add("implementation", project(":core:designsystem"))
                add("implementation", project(":core:data"))
                add("implementation", project(":core:model"))
                add("implementation", project(":core:domain"))

                add("testImplementation", kotlin("test"))
                add("testImplementation", project(":core:testing"))
                add("androidTestImplementation", kotlin("test"))
                add("androidTestImplementation", project(":core:testing"))
                add("androidTestImplementation", project(":core:designsystem"))

                add("implementation", libs.findLibrary("coil.kt").get())
                add("implementation", libs.findLibrary("coil.kt.compose").get())
                add("implementation", libs.findLibrary("coil.kt.svg").get())

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())

                add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
    }
}
