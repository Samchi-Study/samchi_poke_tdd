plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.samchi.poke.feature.jungwon"
    compileSdk = 35
    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(project(":network"))
    testImplementation(project(":network"))
    testImplementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.compose.hilt.navigation)

    testImplementation(libs.test.coroutine)
    implementation(libs.coil)
}
