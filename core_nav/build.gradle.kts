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
                implementation(project(Modules.core))

                implementation(CommonDependencies.koin_core)

                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(Compose.material_motion)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidDependencies.core)
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
        val debug by getting {
            matchingFallbacks += listOf("release", "debug")
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
//    implementation(core())
//
//    //Dagger
//    implementation(AndroidDependencies.hilt)
//    kapt(AndroidDependencies.hilt_compiler)
//    kapt(AndroidDependencies.hilt_androidx_compiler)
//    implementation(AndroidDependencies.hilt_work)
//
//    implementation(Compose.ui)
//    implementation(Compose.foundation)
//    implementation(Compose.material_motion)
//
//    implementation(AndroidDependencies.core)
//
//    //implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"
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