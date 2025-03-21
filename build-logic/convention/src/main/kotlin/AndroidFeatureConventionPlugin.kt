import com.android.build.gradle.LibraryExtension
import com.samchi.poke.samples.app.configureKotlinAndroid
import com.samchi.poke.samples.app.libs
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
                apply("com.samchi.poke.hilt.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)
                defaultConfig.targetSdk = 35
            }

            dependencies {
                add("implementation", project(":model"))
                add("implementation", project(":network"))

                //androidx
                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("implementation", libs.findLibrary("androidx-appcompat").get())
                add("implementation", libs.findLibrary("material").get())
                add("implementation", libs.findLibrary("coil-compose").get())
                add("implementation", libs.findLibrary("coil-network").get())

                //test
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("test-mockk").get())
                add("testImplementation", libs.findLibrary("test-mockk-android").get())
                add("testImplementation", libs.findLibrary("test-mockk-coroutine-core").get())
                add("testImplementation", libs.findLibrary("test-core-ktx").get())
                add("testImplementation", libs.findLibrary("test-ext-junit-ktx").get())
                add("testImplementation", libs.findLibrary("test-robolectric").get())
                add("testImplementation", libs.findLibrary("test-coroutine").get())
                add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
            }
        }
    }
}
