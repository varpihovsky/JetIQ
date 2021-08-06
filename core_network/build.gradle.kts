plugins {
    id(Plugins.android_library)
    kotlin("android")
    kotlin("kapt")
    id(Plugins.hilt)
}

group = Config.group
version = Config.version

android {
    compileSdkVersion(AndroidConfig.compile_sdk)
    defaultConfig {
        minSdkVersion(AndroidConfig.min_sdk)
        targetSdkVersion(AndroidConfig.target_sdk)

        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = AndroidConfig.release_minify
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(repoData())

    //Dagger
    implementation(AndroidDependencies.hilt)
    kapt(AndroidDependencies.hilt_compiler)
    kapt(AndroidDependencies.hilt_androidx_compiler)
    implementation(AndroidDependencies.hilt_work)

    // Retrofit
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofit_converter)
    implementation(Dependencies.retrofit_logging)

    // JUnit
    testImplementation(TestDependencies.junit)
    androidTestImplementation(TestDependencies.junit_ext)
    androidTestImplementation(TestDependencies.compose_test)
    testImplementation(TestDependencies.core_testing)
    testImplementation(TestDependencies.coroutines_test)

    // Mockk
    testImplementation(TestDependencies.mockk)
    androidTestImplementation(TestDependencies.mockk_android)
}