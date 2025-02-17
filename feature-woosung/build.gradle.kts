plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization")
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
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.paging3)
    implementation(libs.paging3.compose)
}
