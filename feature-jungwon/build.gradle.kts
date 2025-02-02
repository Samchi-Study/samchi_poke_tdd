plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.samchi.poke.feature.jungwon"
    compileSdk = 35
    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(project(":network"))
    testImplementation(project(":network"))
    testImplementation(libs.kotlinx.serialization.json)
    
    // 코루틴 테스트 의존성 추가
    testImplementation(libs.test.coroutine)
}
