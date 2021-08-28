plugins {
    androidLib()
    multiplatform()
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
                implementation(project(Modules.core))
                implementation(project(Modules.core_ui))
                implementation(project(Modules.core_lifecycle))
                implementation(project(Modules.core_repo))
                api(project(Modules.feature_profile))
                api(project(Modules.feature_messages))
                api(project(Modules.feature_auth))
                api(project(Modules.feature_settings))

                implementation(kotlin("stdlib-common"))

                api(compose.ui)
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.uiTooling)
                api(compose.materialIconsExtended)

                api(Compose.decompose)
                api(Compose.decompose_extensions)
                api(Compose.material_motion)

                api(CommonDependencies.koin_core)

                api(AndroidDependencies.essenty_parcelize)
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
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}