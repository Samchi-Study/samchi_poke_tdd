plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.samchi.poke.kanghwi"

    hilt {
        enableAggregatingTask = true
    }
}
