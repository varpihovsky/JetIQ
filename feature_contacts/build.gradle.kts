import org.jetbrains.compose.compose

plugins {
    multiplatform()
    feature()
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
                implementation(project(Modules.ui_data))
                implementation(project(Modules.repo_data))

                implementation(CommonDependencies.koin_core)

                implementation(compose.ui)
                implementation(compose.material)
                implementation(compose.foundation)
                implementation(Compose.material_motion)
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