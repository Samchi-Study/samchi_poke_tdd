plugins {
    id("com.samchi.poke.feature")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.samchi.poke.feature.kanghwi"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}