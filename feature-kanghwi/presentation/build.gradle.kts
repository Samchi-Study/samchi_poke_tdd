plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.samchi.poke.kanghwi.presentation"

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(project(":feature-kanghwi:model"))
    implementation(project(":feature-kanghwi:data"))
    implementation(project(":common"))

    implementation(libs.paging3)
    implementation(libs.paging3.compose)
}
