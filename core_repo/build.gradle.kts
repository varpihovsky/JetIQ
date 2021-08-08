import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id(Plugins.android_library)
    kotlin("kapt")
}

group = Config.group
version = Config.version

kotlin {
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(Modules.core))
                implementation(project(Modules.core_network))
                implementation(project(Modules.core_db))
                implementation(project(Modules.repo_data))

                implementation(compose.ui)
                implementation(CommonDependencies.coroutines)
                implementation(CommonDependencies.koin_core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":core_test"))

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(TestDependencies.mockk)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidDependencies.preferences)
                implementation(AndroidDependencies.koin_android)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(project(":core_test"))

                implementation(kotlin("test"))
                implementation(TestDependencies.core_testing)
                implementation(TestDependencies.coroutines_test)
                implementation(TestDependencies.mockk)
            }
        }
    }
}

android {
    compileSdkVersion(AndroidConfig.compile_sdk)
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
}

//dependencies {
//    implementation(core())
//    implementation(coreNetwork())
//    implementation(coreDB())
//    implementation(repoData())
//
//    implementation(AndroidDependencies.core)
//
//    // JUnit
//    testImplementation(TestDependencies.junit)
//    androidTestImplementation(TestDependencies.junit_ext)
//    androidTestImplementation(TestDependencies.compose_test)
//    testImplementation(TestDependencies.core_testing)
//    testImplementation(TestDependencies.coroutines_test)
//
//    // Preferences
//    implementation(AndroidDependencies.preferences)
//
//    // Mockk
//    testImplementation(TestDependencies.mockk)
//    androidTestImplementation(TestDependencies.mockk_android)
//
//    implementation(Compose.ui)
//}