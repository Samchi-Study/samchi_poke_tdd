import com.android.build.gradle.LibraryExtension
import com.samchi.poke.samples.app.androidTestImplementation
import com.samchi.poke.samples.app.configureKotlinAndroid
import com.samchi.poke.samples.app.libs
import com.samchi.poke.samples.app.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies


class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 35
            }

            dependencies {
                testImplementation(libs.findLibrary("junit").get())
                testImplementation(libs.findLibrary("test-mockk").get())
                testImplementation(libs.findLibrary("test-mockk-android").get())
                testImplementation(libs.findLibrary("test-mockk-coroutine-core").get())
                testImplementation(libs.findLibrary("test-core-ktx").get())
                testImplementation(libs.findLibrary("test-ext-junit-ktx").get())
                testImplementation(libs.findLibrary("test-robolectric").get())
                testImplementation(libs.findLibrary("test-coroutine").get())
                androidTestImplementation(libs.findLibrary("androidx-junit").get())
                androidTestImplementation(libs.findLibrary("androidx-espresso-core").get())
            }
        }
    }
}
