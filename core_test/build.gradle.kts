plugins {
    kotlin("multiplatform")
    id(Plugins.android_library)
}

group = Config.group
version = Config.version

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}")

                // Mockk
                implementation(TestDependencies.mockk)
                implementation(CommonDependencies.coroutines)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(TestDependencies.core_testing)
                implementation(TestDependencies.mockk)

                implementation(TestDependencies.coroutines_test)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(TestDependencies.core_testing)
                implementation(TestDependencies.mockk)

                implementation(TestDependencies.coroutines_test)
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
