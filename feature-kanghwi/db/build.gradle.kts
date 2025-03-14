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
    ksp(libs.room.compiler)
}
