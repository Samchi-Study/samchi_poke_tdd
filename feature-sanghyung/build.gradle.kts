plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.samchi.feature.sanghyung"

    hilt {
        enableAggregatingTask = true
    }
}
