import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}
plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidFeature") {
            id = "com.samchi.poke.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidHilt"){
            id = "com.samchi.poke.hilt.android"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}
