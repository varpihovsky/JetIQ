plugins {
    id(Plugins.android_application)
    kotlin("android")
    kotlin("kapt")
    parcelize()
    id(Plugins.hilt)
}

android {
    compileSdkVersion(AndroidConfig.compile_sdk)
    defaultConfig {
        applicationId = AndroidConfig.application_id
        minSdkVersion(AndroidConfig.min_sdk)
        targetSdkVersion(AndroidConfig.target_sdk)
        versionCode = AndroidConfig.version_code
        versionName = AndroidConfig.application_version
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
    implementation(core())
    implementation(coreRepo())
    implementation(coreNav())
    implementation(uiData())
    implementation(repoData())

    // Needed for dependency injection
    api(coreDB())
    api(coreNetwork())

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
    implementation(Compose.material)
    implementation(Compose.ui_tooling)

    // Calpose
    implementation(Compose.calpose)
    implementation(AndroidDependencies.threetenbp)

    // Retrofit
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofit_converter)

    //Dagger
    implementation(AndroidDependencies.hilt)
    kapt(AndroidDependencies.hilt_compiler)
    kapt(AndroidDependencies.hilt_androidx_compiler)
    implementation(AndroidDependencies.hilt_work)

    // Coil
    implementation(AndroidDependencies.coil)
    implementation(AndroidDependencies.coil_base)
    implementation(AndroidDependencies.coil_compose)

    // Accompanist
    implementation(AndroidDependencies.swipe_refresh)

    // WorkManager
    implementation(AndroidDependencies.work)

    // JUnit
    testImplementation(TestDependencies.junit)
    androidTestImplementation(TestDependencies.junit_ext)
    androidTestImplementation(TestDependencies.compose_test)
    testImplementation(TestDependencies.core_testing)
    testImplementation(TestDependencies.coroutines_test)

    // Mockk
    testImplementation(TestDependencies.mockk)
    androidTestImplementation(TestDependencies.mockk_android)

    // Espresso
    androidTestImplementation(TestDependencies.espresso)

    implementation(Compose.material_motion)
    implementation(AndroidDependencies.preferences)
}
