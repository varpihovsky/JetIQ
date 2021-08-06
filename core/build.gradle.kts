plugins {
    id(Plugins.android_library)
    kotlin("android")
    kotlin("kapt")
    id(Plugins.hilt)
    parcelize()
}

group = Config.group
version = Config.version

android {
    compileSdkVersion(AndroidConfig.compile_sdk)
    buildToolsVersion = AndroidConfig.build_tools_version

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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose_version
    }
}

dependencies {
    //Dagger
    implementation(AndroidDependencies.hilt)
    kapt(AndroidDependencies.hilt_compiler)
    kapt(AndroidDependencies.hilt_androidx_compiler)
    implementation(AndroidDependencies.hilt_work)

    // For local unit tests
    testImplementation(AndroidDependencies.hilt_testing)
    kaptTest(AndroidDependencies.hilt_compiler)

    // Core
    implementation(AndroidDependencies.core)
    implementation(AndroidDependencies.app_compat)
    implementation(AndroidDependencies.material)

    // Architecture components
    implementation(Compose.lifecycle_runtime)
    implementation(Compose.activity_compose)
    implementation(Compose.view_model_compose)
    implementation(Compose.compose_livedata)

    // Compose
    implementation(Compose.ui)
    implementation(Compose.foundation_layout)

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