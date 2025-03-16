plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.samchi.feature.sanghyeong"

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(project(":network"))

    implementation(libs.kotlinx.immutable)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    implementation(libs.paging3)
    implementation(libs.paging3.compose)
}
