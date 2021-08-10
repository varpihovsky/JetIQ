plugins {
    id(Plugins.android_application)
    kotlin("android")
    kotlin("kapt")
    parcelize()
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
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    dynamicFeatures += setOf(
        ":feature_profile", ":feature_messages", ":feature_contacts",
        ":feature_new_message", ":feature_auth", ":feature_settings", ":feature_subjects"
    )
}

dependencies {
    implementation(core())
    implementation(coreRepo())
    implementation(coreNav())
    implementation(uiData())
    implementation(repoData())
    implementation(project(Modules.core_ui))
    implementation(project(Modules.feature_auth))
    implementation(project(Modules.feature_contacts))
    implementation(project(Modules.feature_messages))
    implementation(project(Modules.feature_new_message))
    implementation(project(Modules.feature_profile))
    implementation(project(Modules.feature_settings))
    implementation(project(Modules.feature_subjects))

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
    //implementation(Compose.calpose)
    implementation(AndroidDependencies.threetenbp)

    // Retrofit
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofit_converter)

    //koin
    implementation(CommonDependencies.koin_core)
    implementation(AndroidDependencies.koin_android)
    implementation(AndroidDependencies.koin_work_manager)

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

    implementation(AndroidDependencies.preferences)
    implementation(Compose.material_motion)
}