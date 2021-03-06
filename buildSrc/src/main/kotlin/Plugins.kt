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

import org.gradle.kotlin.dsl.kotlin
import org.gradle.plugin.use.PluginDependenciesSpec

object Plugins {
    const val android_application = "com.android.application"
    const val android_library = "com.android.library"

    const val hilt = "dagger.hilt.android.plugin"

    const val compose_multiplatform = "org.jetbrains.compose"

    const val feature = "com.android.dynamic-feature"
}

fun PluginDependenciesSpec.parcelize() = id("kotlin-parcelize")
fun PluginDependenciesSpec.feature() = id(Plugins.android_library)
fun PluginDependenciesSpec.compose() = id(Plugins.compose_multiplatform)
fun PluginDependenciesSpec.multiplatform() = kotlin("multiplatform")
fun PluginDependenciesSpec.androidLib() = id(Plugins.android_library)
fun PluginDependenciesSpec.androidApp() = id(Plugins.android_application)