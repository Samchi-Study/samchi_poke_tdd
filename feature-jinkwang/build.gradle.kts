plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.samchi.poke.feature.jinkwang"

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    implementation(libs.paging3)
    implementation(libs.paging3.compose)
    ksp(libs.room.compiler)
    implementation(project(":network"))
    implementation(project(":model"))
}