plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
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
    implementation(libs.coil.compose)
}
