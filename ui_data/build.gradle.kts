plugins {
    id(Plugins.android_library)
    kotlin("android")
    parcelize()
}

group = Config.group
version = Config.version

android {
    compileSdkVersion(AndroidConfig.compile_sdk)
    defaultConfig {
        minSdkVersion(AndroidConfig.min_sdk)
        targetSdkVersion(AndroidConfig.target_sdk)

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
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose_version
    }
}

dependencies {
    implementation(repoData())

    implementation(AndroidDependencies.threetenbp)

    implementation(AndroidDependencies.core)
    implementation(Compose.ui)
    implementation(Compose.foundation)
    implementation(AndroidDependencies.swipe_refresh)

}