plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.samchi.poke.feature.jungwon"
    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(project(":network"))
    testImplementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.compose.hilt.navigation)
    implementation(libs.coil)
}
