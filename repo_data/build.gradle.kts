import extensions.kapt

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
            }
        }
        val androidMain by getting {
            dependencies {
                // Room
                implementation(AndroidDependencies.room_runtime)
                implementation(AndroidDependencies.room)
                kapt(AndroidDependencies.room_compiler)

                implementation(Dependencies.retrofit_converter)
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

//        consumerProguardFiles("consumer-rules.pro")
    }
//    buildTypes {
//        getByName("release") {
//            isMinifyEnabled = AndroidConfig.release_minify
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}