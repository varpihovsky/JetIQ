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


import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.KotlinSettingsScript
import org.gradle.kotlin.dsl.project

object Modules {
    const val app = ":app"
    const val core = ":core"
    const val core_db = ":core_db"
    const val core_nav = ":core_nav"
    const val core_network = ":core_network"
    const val core_repo = ":core_repo"
    const val core_ui = ":core_ui"
    const val core_lifecycle = ":core_lifecycle"
    const val core_test = ":core_test"
    const val repo_data = ":repo_data"
    const val ui_data = ":ui_data"
    const val feature_contacts = ":feature_contacts"
}

fun KotlinSettingsScript.includeModules() {
    include(Modules.app)
    include(Modules.core_repo)
    include(Modules.core)
    include(Modules.ui_data)
    include(Modules.core_network)
    include(Modules.core_db)
    include(Modules.repo_data)
    include(Modules.core_nav)
}

fun DependencyHandlerScope.app() = project(Modules.app)

fun DependencyHandlerScope.core() = project(Modules.core)

fun DependencyHandlerScope.coreDB() = project(Modules.core_db)

fun DependencyHandlerScope.coreRepo() = project(Modules.core_repo)

fun DependencyHandlerScope.coreNav() = project(Modules.core_nav)

fun DependencyHandlerScope.coreNetwork() = project(Modules.core_network)

fun DependencyHandlerScope.repoData() = project(Modules.repo_data)

fun DependencyHandlerScope.uiData() = project(Modules.ui_data)