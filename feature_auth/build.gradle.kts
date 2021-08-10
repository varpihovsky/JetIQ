import org.jetbrains.compose.compose

plugins {
    multiplatform()
    feature()
    compose()
}

group = Config.group
version = Config.version

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

                implementation(CommonDependencies.koin_core)

                implementation(compose.ui)
                implementation(compose.material)
                implementation(compose.foundation)
            }
        }
    }
}
android {
    compileSdk = AndroidConfig.compile_sdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
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