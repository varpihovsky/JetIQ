plugins {
    multiplatform()
    androidLib()
    kotlin("kapt")
    kotlin("plugin.serialization")
}

group = Config.group
version = Config.version

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(CommonDependencies.kodein)

                implementation(CommonDependencies.serialization)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidDependencies.core)
            }
        }
        val jvmMain by getting
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