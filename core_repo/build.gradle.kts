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
                implementation(project(Modules.core_db))
                implementation(project(Modules.repo_data))

                implementation(project(Modules.core_network))

                api(compose.ui)
                implementation(CommonDependencies.coroutines)
                implementation(CommonDependencies.koin_core)

                implementation(CommonDependencies.kodein)
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