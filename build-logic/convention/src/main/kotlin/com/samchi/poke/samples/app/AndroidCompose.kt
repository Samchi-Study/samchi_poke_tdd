import com.android.build.api.dsl.CommonExtension
import com.samchi.poke.samples.app.debugImplementation
import com.samchi.poke.samples.app.findVersionCatalog
import com.samchi.poke.samples.app.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    val libs = findVersionCatalog()

    commonExtension.apply {
        buildFeatures.compose = true
    }

    dependencies {
        implementation(platform(libs.findLibrary("androidx-compose-bom").get()))
        implementation(libs.findBundle("compose"))
        debugImplementation(libs.findBundle("compose-preview"))
    }
}
