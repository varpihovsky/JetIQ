import org.jetbrains.compose.compose

plugins {
    id(Plugins.android_application)
    kotlin("android")
    kotlin("kapt")
    compose()
    parcelize()
}

android {
    compileSdk = AndroidConfig.compile_sdk
    defaultConfig {
        applicationId = AndroidConfig.application_id
        minSdk = AndroidConfig.min_sdk
        targetSdk = AndroidConfig.target_sdk
        versionCode = AndroidConfig.version_code
        versionName = AndroidConfig.application_version
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

configurations {
    "implementation" {
        exclude(group = "androidx.compose.animation")
        exclude(group = "androidx.compose.foundation")
        exclude(group = "androidx.compose.material")
        exclude(group = "androidx.compose.runtime")
        exclude(group = "androidx.compose.ui")
    }
}

dependencies {
    implementation(core())
    implementation(coreRepo())
    implementation(uiData())
    implementation(repoData())
    implementation(project(Modules.core_ui))
    implementation(project(Modules.core_lifecycle))
    implementation(project(Modules.feature_auth))
    implementation(project(Modules.feature_messages))
    implementation(project(Modules.feature_profile))
    implementation(project(Modules.core_network))
    implementation(project(Modules.ui_root))

    // Needed for dependency injection
    api(coreDB())

    // Core
    implementation(AndroidDependencies.core)
    implementation(AndroidDependencies.app_compat)
    implementation(AndroidDependencies.material)

    // Architecture components
    implementation(Compose.lifecycle_runtime)
    implementation(Compose.activity_compose)
    implementation(Compose.view_model_compose)
    implementation(Compose.compose_livedata)
    implementation(AndroidDependencies.lifecycle_view_model)

    // Compose
    api(compose.ui)
    api(compose.material)
    api(compose.uiTooling)
    implementation(Compose.material_motion)

    //koin
    implementation(CommonDependencies.koin_core)
    implementation(AndroidDependencies.koin_android)
    implementation(AndroidDependencies.koin_work_manager)

    // WorkManager
    implementation(AndroidDependencies.work)

    // JUnit
    testImplementation(TestDependencies.junit)
    androidTestImplementation(TestDependencies.junit_ext)
    testImplementation(TestDependencies.core_testing)
    testImplementation(TestDependencies.coroutines_test)

    // Mockk
    testImplementation(TestDependencies.mockk)
    androidTestImplementation(TestDependencies.mockk_android)

    // Kodein
    debugImplementation(AndroidDependencies.kodein_debug)
    releaseImplementation(AndroidDependencies.kodein_release)

    implementation(Compose.decompose_extensions)
}