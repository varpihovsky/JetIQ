/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

object Versions {
    const val gradle_version = "7.0.0"

    const val kotlin_version = "1.5.10"
    const val compose_version = "1.0.0"
    const val mockk_version = "1.12.0"
    const val room_version = "2.4.0-alpha04"
    const val accompanist_version = "0.15.0"
    const val retrofit_version = "2.9.0"
    const val coil_version = "1.3.1"
    const val androidx_core_version = "1.6.0"
    const val appcompat_version = "1.3.1"
    const val material_version = "1.4.0"
    const val preferences_version = "1.0.0-rc02"
    const val material_motion_version = "0.6.1"
    const val threetenbp_version = "1.5.1"
    const val lifecycle_runtime_version = "2.3.1"
    const val view_model_compose_version = "1.0.0-alpha07"

    const val dagger_version = "2.38.1"
    const val dagger_additionals_version = "1.0.0"

    const val koin_version = "3.1.2"

    const val activity_compose_version = "1.3.1"

    const val junit_version = "4.13.2"
    const val junit_ext_version = "1.1.3"
    const val coroutines_testing_version = "1.5.1"
    const val core_testing_version = "2.1.0"

    const val kamel_version = "0.2.2"
}

object Config {
    const val group = "com.varpihovsky"
    const val version = ""
}

object AndroidConfig {
    const val application_id = "${Config.group}.jetiq"

    const val application_version = Config.version
    const val min_sdk = 23
    const val target_sdk = 30
    const val version_code = 4
    const val compile_sdk = 30
    const val build_tools_version = "30.0.3"

    const val release_minify = true
}

object RootDependencies {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle_version}"
    const val kotlin_gradle_plugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_version}"
    const val compose_gradle_plugin =
        "org.jetbrains.compose:compose-gradle-plugin:${Versions.compose_version}"
    const val hilt_gradle_plugin =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.dagger_version}"
}

object Compose {
    const val lifecycle_runtime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle_runtime_version}"
    const val activity_compose =
        "androidx.activity:activity-compose:${Versions.activity_compose_version}"
    const val compose_livedata =
        "androidx.compose.runtime:runtime-livedata:${Versions.compose_version}"
    const val ui = "androidx.compose.ui:ui:${Versions.compose_version}"
    const val ui_tooling = "androidx.compose.ui:ui-tooling:${Versions.compose_version}"
    const val material = "androidx.compose.material:material:${Versions.compose_version}"
    const val foundation = "androidx.compose.foundation:foundation:${Versions.compose_version}"
    const val foundation_layout =
        "androidx.compose.foundation:foundation-layout:${Versions.compose_version}"

    const val koin_compose = "io.insert-koin:koin-androidx-compose:${Versions.koin_version}"

    const val compiler = "androidx.compose.compiler:compiler:${Versions.compose_version}"

    const val view_model_compose =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.view_model_compose_version}"

    const val material_motion =
        "soup.compose.material:library:0.6.4"
    const val calpose = "com.github.sigmadeltasoftware:CalPose:master-SNAPSHOT"
}

object Dependencies {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit_version}"
    const val retrofit_converter =
        "com.squareup.retrofit2:converter-gson:${Versions.retrofit_version}"
    const val retrofit_logging = "com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2"
}

object CommonDependencies {
    const val koin_core = "io.insert-koin:koin-core:${Versions.koin_version}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1"
    const val kamel = "com.alialbaali.kamel:kamel-image:${Versions.kamel_version}"
}

object AndroidDependencies {
    const val core = "androidx.core:core-ktx:${Versions.androidx_core_version}"
    const val app_compat = "androidx.appcompat:appcompat:${Versions.appcompat_version}"
    const val material = "com.google.android.material:material:${Versions.material_version}"

    const val koin_android = "io.insert-koin:koin-android:${Versions.koin_version}"
    const val koin_work_manager =
        "io.insert-koin:koin-androidx-workmanager:${Versions.koin_version}"

    const val room = "androidx.room:room-ktx:${Versions.room_version}"
    const val room_runtime = "androidx.room:room-runtime:${Versions.room_version}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room_version}"
    const val room_testing = "androidx.room:room-testing:${Versions.room_version}"

    const val preferences =
        "androidx.datastore:datastore-preferences:${Versions.preferences_version}"
    const val work = "androidx.work:work-runtime-ktx:2.5.0"
    const val threetenbp = "org.threeten:threetenbp:${Versions.threetenbp_version}"

    const val swipe_refresh =
        "com.google.accompanist:accompanist-swiperefresh:${Versions.accompanist_version}"

    const val lifecycle_view_model = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
}

object TestDependencies {
    const val junit = "junit:junit:${Versions.junit_version}"
    const val junit_ext = "androidx.test.ext:junit:${Versions.junit_ext_version}"
    const val compose_test = "androidx.compose.ui:ui-test-junit4:${Versions.compose_version}"
    const val core_testing = "androidx.arch.core:core-testing:${Versions.core_testing_version}"
    const val coroutines_test =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines_testing_version}"

    const val mockk = "io.mockk:mockk:${Versions.mockk_version}"
    const val mockk_android = "io.mockk:mockk-android:${Versions.mockk_version}"

    const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
}