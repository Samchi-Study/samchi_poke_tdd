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
    testImplementation(libs.paging.test.android)
    implementation(libs.test.mockk)

    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    androidTestImplementation(libs.test.coroutine.get())
}
