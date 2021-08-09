import org.jetbrains.compose.compose

plugins {
    multiplatform()
    androidLib()
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
                implementation(project(Modules.core_nav))

                implementation(compose.foundation)
                implementation(compose.runtime)
            }
        }
    }
}

android {
    compileSdk = AndroidConfig.compile_sdk
    sourceSets["main"].manifest.srcFile("src/commonMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidConfig.min_sdk
        targetSdk = AndroidConfig.target_sdk

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
        val debug by getting {
            matchingFallbacks += listOf("release", "debug")
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose_version
    }
}