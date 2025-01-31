plugins {
    alias(libs.plugins.samchi.poke.library)
}

android {
    namespace = "com.samchi.poke.kanghwi.common"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}