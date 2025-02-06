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
    implementation(project(":model"))
    implementation(project(":feature-kanghwi:data"))
    implementation(project(":feature-kanghwi:common"))
}
