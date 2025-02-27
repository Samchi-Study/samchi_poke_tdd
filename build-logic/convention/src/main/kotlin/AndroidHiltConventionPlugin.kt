import com.samchi.poke.samples.app.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


class AndroidHiltConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("dagger.hilt.android.plugin")
            }

            dependencies {
                add("implementation", libs.findLibrary("hilt-android").get())
                add("implementation", libs.findLibrary("hilt.core").get())
                add("ksp", libs.findLibrary("hilt-compiler").get())
            }
        }
    }
}
