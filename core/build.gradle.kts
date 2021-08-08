import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id(Plugins.android_library)
    kotlin("kapt")
    parcelize()
}

group = Config.group
version = Config.version

kotlin {
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(CommonDependencies.koin_core)

                implementation(compose.runtime)

                implementation(compose.ui)
                implementation(compose.foundation)

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(Modules.core_test))

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                // JUnit
                //implementation(TestDependencies.junit)
                //implementation(TestDependencies.coroutines_test)

                // Mockk
                implementation(TestDependencies.mockk)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidDependencies.koin_android)

                implementation(Compose.lifecycle_runtime)
                implementation(Compose.view_model_compose)
            }
        }
    }
}

android {
    compileSdkVersion(AndroidConfig.compile_sdk)
    buildToolsVersion = AndroidConfig.build_tools_version
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
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

//dependencies {
//    //Dagger
//    implementation(AndroidDependencies.hilt)
//    kapt(AndroidDependencies.hilt_compiler)
//    kapt(AndroidDependencies.hilt_androidx_compiler)
//    implementation(AndroidDependencies.hilt_work)
//
//    // For local unit tests
//    testImplementation(AndroidDependencies.hilt_testing)
//    kaptTest(AndroidDependencies.hilt_compiler)
//
//    // Core
//    implementation(AndroidDependencies.core)
//    implementation(AndroidDependencies.app_compat)
//    implementation(AndroidDependencies.material)
//
//    implementation(Compose.activity_compose)
//    implementation(Compose.view_model_compose)
//    implementation(Compose.compose_livedata)
//
//    // JUnit
//    testImplementation(TestDependencies.junit)
//    androidTestImplementation(TestDependencies.junit_ext)
//    androidTestImplementation(TestDependencies.compose_test)
//    testImplementation(TestDependencies.core_testing)
//    testImplementation(TestDependencies.coroutines_test)
//
//    // Mockk
//    testImplementation(TestDependencies.mockk)
//    androidTestImplementation(TestDependencies.mockk_android)
//}