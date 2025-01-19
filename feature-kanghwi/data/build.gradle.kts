plugins {
    alias(libs.plugins.samchi.poke.library)
    alias(libs.plugins.samchi.poke.hilt)
}

android {
    namespace = "com.samchi.poke.kanghwi.data"

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
}
