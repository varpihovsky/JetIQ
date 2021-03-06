import org.jetbrains.compose.compose

plugins {
    multiplatform()
    androidLib()
    compose()
    parcelize()
}

group = Config.group
version = Config.version

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(Modules.core))

                api(compose.foundation)
                api(compose.runtime)

                api(Compose.decompose)
                api(Compose.decompose_extensions)
            }
        }
        val androidMain by getting {
            dependencies {
                api(AndroidDependencies.lifecycle_view_model)
            }
        }
        val jvmMain by getting
    }
}

android {
    compileSdk = AndroidConfig.compile_sdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidConfig.min_sdk
        targetSdk = AndroidConfig.target_sdk

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