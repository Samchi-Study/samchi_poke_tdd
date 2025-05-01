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
    implementation(project(":network"))
    implementation(project(":feature-kanghwi:db"))
    implementation(project(":feature-kanghwi:model"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.paging3)
    implementation(libs.paging3.room)

    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.paging.test.android)
    testImplementation(libs.paging3.common)
    testImplementation(kotlin("test"))
}
