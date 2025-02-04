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

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.test.mockk.android)
    testImplementation(libs.test.mockk.coroutine.core)
}
