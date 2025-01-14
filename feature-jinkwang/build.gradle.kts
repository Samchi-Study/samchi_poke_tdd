plugins {
    id("com.samchi.poke.feature")
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("com.samchi.poke.hilt")
}

android {
    namespace = "com.samchi.poke.feature.jinkwang"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}