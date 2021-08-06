plugins {
    id(Plugins.android_library)
    kotlin("android")
    kotlin("kapt")
    id(Plugins.hilt)
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

    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(repoData())

    //Dagger
    implementation(AndroidDependencies.hilt)
    kapt(AndroidDependencies.hilt_compiler)
    kapt(AndroidDependencies.hilt_androidx_compiler)
    implementation(AndroidDependencies.hilt_work)

    // Room
    implementation(AndroidDependencies.room_runtime)
    implementation(AndroidDependencies.room)
    kapt(AndroidDependencies.room_compiler)
    testImplementation(AndroidDependencies.room_testing)

    implementation(AndroidDependencies.core)
}