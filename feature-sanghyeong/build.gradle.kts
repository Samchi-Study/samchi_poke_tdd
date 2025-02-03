plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.samchi.feature.sanghyeong"

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(project(":network"))
}
