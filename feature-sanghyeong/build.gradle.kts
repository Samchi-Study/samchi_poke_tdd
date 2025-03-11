plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.samchi.feature.sanghyeong"

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(project(":network"))

    implementation(libs.kotlinx.immutable)
}
