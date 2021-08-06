import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id(Plugins.android_library)
    id(Plugins.compose_multiplatform)
    parcelize()
}

group = Config.group
version = Config.version

kotlin {
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(Modules.repo_data))

                api(compose.ui)
                api(compose.foundation)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidDependencies.threetenbp)
                implementation(AndroidDependencies.core)

                implementation(AndroidDependencies.swipe_refresh)
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose_version
    }
}

//dependencies {
//    implementation(repoData())
//
//    implementation(AndroidDependencies.threetenbp)
//
//    implementation(AndroidDependencies.core)
//    implementation(Compose.ui)
//    implementation(Compose.foundation)
//    implementation(AndroidDependencies.swipe_refresh)
//
//}