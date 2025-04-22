plugins {
    alias(libs.plugins.samchi.poke.library)
    alias(libs.plugins.samchi.poke.hilt)
}

android {
    namespace = "com.samchi.poke.kanghwi.db"


    hilt {
        enableAggregatingTask = true
    }
}

dependencies {

    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    implementation(libs.paging3.common)
    implementation(libs.paging3.room)
    ksp(libs.room.compiler)
}
