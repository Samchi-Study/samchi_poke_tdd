plugins {
    alias(libs.plugins.samchi.poke.library)
    alias(libs.plugins.samchi.poke.hilt)
    alias(libs.plugins.samchi.poke.room)
}

android {
    namespace = "com.samchi.poke.kanghwi.db"

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(libs.paging3.common)
    implementation(libs.paging3.room)
}
