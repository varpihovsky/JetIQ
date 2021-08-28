import org.jetbrains.compose.compose

plugins {
    multiplatform()
    feature()
    compose()
    id("kotlin-parcelize")
}

group = Config.group
version = Config.version

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(Modules.core_ui))
                implementation(project(Modules.core))
                implementation(project(Modules.core_repo))
                implementation(project(Modules.core_lifecycle))
                implementation(project(Modules.ui_data))
                implementation(project(Modules.repo_data))
                implementation(project(Modules.core_network))

                implementation(CommonDependencies.koin_core)
                api(CommonDependencies.date_time)

                api(compose.ui)
                api(compose.material)
                api(compose.foundation)
                api(compose.runtime)
                api(compose.materialIconsExtended)
                api(Compose.material_motion)

                api(AndroidDependencies.essenty_parcelize)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidDependencies.swipe_refresh)
                implementation(AndroidDependencies.lifecycle_view_model)
            }
        }
        val jvmMain by getting
    }
}
android {
    compileSdk = AndroidConfig.compile_sdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidConfig.min_sdk
    }

//    buildTypes {
//        release {
//            isMinifyEnabled = false
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