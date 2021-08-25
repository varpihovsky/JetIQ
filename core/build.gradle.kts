import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id(Plugins.android_library)
    kotlin("kapt")
    parcelize()
    compose()
}

group = Config.group
version = Config.version

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(CommonDependencies.koin_core)

                api(compose.runtime)
                api(compose.ui)
                api(compose.foundation)

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(Modules.core_test))

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(TestDependencies.mockk)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidDependencies.koin_android)
                implementation(Compose.koin_compose)

                implementation(AndroidDependencies.lifecycle_view_model)
            }
        }
        val jvmMain by getting
    }
}

android {
    compileSdk = AndroidConfig.compile_sdk
    buildToolsVersion = AndroidConfig.build_tools_version
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}