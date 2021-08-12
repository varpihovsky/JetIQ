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
                implementation(project(Modules.repo_data))

                implementation(CommonDependencies.koin_core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":core_test"))

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(TestDependencies.coroutines_test)
                implementation(TestDependencies.mockk)
            }
        }
        val androidMain by getting {
            dependencies {
                // Retrofit
                implementation(Dependencies.retrofit)
                implementation(Dependencies.retrofit_converter)
                implementation(Dependencies.retrofit_logging)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(project(":core_test"))

                implementation(kotlin("test"))
                implementation(TestDependencies.core_testing)
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
