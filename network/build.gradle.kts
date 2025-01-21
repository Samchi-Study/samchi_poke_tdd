plugins {
    alias(libs.plugins.samchi.poke.library)
    alias(libs.plugins.samchi.poke.hilt)
    kotlin("plugin.serialization")
}

android {
    namespace = "com.samchi.poke.network"

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.network.interceptor)

    testImplementation(libs.test.mock.server)
}