import com.android.build.gradle.LibraryExtension
import com.samchi.poke.samples.app.configureKotlinAndroid
import com.samchi.poke.samples.app.findVersionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)
                defaultConfig.targetSdk = 34
            }
            dependencies {
                add("implementation", findVersionCatalog().findLibrary("kotlinx.serialization.json").get())
            }
        }
    }
}