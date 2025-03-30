plugins {
    alias(libs.plugins.samchi.poke.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.samchi.poke.feature.jungwon"
    hilt {
        enableAggregatingTask = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.maxHeapSize = "1024m"
            }
        }
    }
}

dependencies {
    implementation(project(":network"))
    testImplementation(libs.kotlinx.serialization.json)
    implementation(libs.coil.compose)
    
    // DataStore
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.preferences.core)
    
    // Testing
    testImplementation(libs.test.coroutine)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.test.core.ktx)
    testImplementation(libs.test.ext.junit.ktx)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.test.core.ktx)
    androidTestImplementation(libs.test.ext.junit.ktx)
}
