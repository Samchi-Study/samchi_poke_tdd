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
    implementation(project(":model"))
    implementation(project(":network"))
    implementation(project(":feature-kanghwi:db"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.paging3)
    implementation(libs.paging.test.android)
    testImplementation(libs.paging3.common)
}
