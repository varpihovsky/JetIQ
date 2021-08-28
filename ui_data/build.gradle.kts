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
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(Modules.repo_data))
                implementation(project(Modules.core_network))

                api(compose.ui)
                api(compose.foundation)

                implementation(CommonDependencies.kodein)

                api(AndroidDependencies.essenty_parcelize)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidDependencies.threetenbp)
                implementation(AndroidDependencies.core)

                implementation(AndroidDependencies.swipe_refresh)
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}