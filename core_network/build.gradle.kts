plugins {
    multiplatform()
    androidLib()
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
                implementation(CommonDependencies.ktor_client)
                implementation(CommonDependencies.ktor_serialization)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
                implementation(kotlin("reflect"))
            }

        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("reflect"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(kotlin("reflect"))
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

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}