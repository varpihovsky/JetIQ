import org.jetbrains.compose.compose

plugins {
    multiplatform()
    feature()
    compose()
}
kotlin {
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(Modules.core_ui))
                implementation(project(Modules.core))
                implementation(project(Modules.core_repo))
                implementation(project(Modules.core_nav))
                implementation(project(Modules.core_lifecycle))

                implementation(compose.ui)
                implementation(compose.material)
                implementation(compose.foundation)

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