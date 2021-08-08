plugins {
    multiplatform()
    feature()
}
kotlin {
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                //implementation(project(":app"))

                implementation(CommonDependencies.koin_core)
            }
        }
    }
}
android {
    compileSdk = AndroidConfig.compile_sdk

    defaultConfig {
        minSdk = AndroidConfig.min_sdk
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}