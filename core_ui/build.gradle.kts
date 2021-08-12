import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id(Plugins.android_library)
    kotlin("kapt")
    compose()
}

group = Config.group
version = Config.version

kotlin {
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(Modules.core))
                implementation(project(Modules.ui_data))
                implementation(project(Modules.repo_data))

                api(compose.ui)
                api(compose.foundation)
                api(compose.material)
                api(compose.runtime)
                implementation(Compose.material_motion)

                implementation(CommonDependencies.kamel)

                implementation(CommonDependencies.coroutines)
                implementation(CommonDependencies.koin_core)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidDependencies.app_compat)
            }
        }
    }
}

android {
    compileSdk = AndroidConfig.compile_sdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidConfig.min_sdk
        targetSdk = AndroidConfig.target_sdk

        //      consumerProguardFiles("consumer-rules.pro")
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
}