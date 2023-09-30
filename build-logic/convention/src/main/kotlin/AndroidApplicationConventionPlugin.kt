import com.android.build.api.dsl.ApplicationExtension
import com.wei.amazingtalker_recruit.configureGradleManagedDevices
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.wei.amazingtalker_recruit.configureKotlinAndroid
import com.wei.amazingtalker_recruit.configurePrintApksTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
                configureGradleManagedDevices(this)
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
            }
        }
    }

}