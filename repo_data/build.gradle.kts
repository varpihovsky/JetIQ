plugins {
    id(Plugins.android_library)
    kotlin("android")
    kotlin("kapt")
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
}

dependencies {
    // Room
    implementation(AndroidDependencies.room_runtime)
    implementation(AndroidDependencies.room)
    kapt(AndroidDependencies.room_compiler)
    testImplementation(AndroidDependencies.room_testing)

    implementation(Dependencies.retrofit_converter)

    implementation(AndroidDependencies.core)
}