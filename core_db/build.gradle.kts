plugins {
    kotlin("multiplatform")
    id(Plugins.android_library)
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
                implementation(project(Modules.core))
                implementation(project(Modules.repo_data))
                implementation(project(Modules.core_network))

                implementation(CommonDependencies.coroutines)

                implementation(CommonDependencies.koin_core)
                implementation(CommonDependencies.kodein)
                implementation(CommonDependencies.serialization)
                implementation(CommonDependencies.kodein_serializer)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidDependencies.koin_android)
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
    }

    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
