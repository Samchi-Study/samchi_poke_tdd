plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.samchi.feature.woosung"

    hilt {
        enableAggregatingTask = true
    }
}

dependencies{
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
}
