package com.varpihovsky.core_nav.dsl

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

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.main.NavigationOperation
import soup.compose.material.motion.MaterialMotion

/**
 * Creates navigation controller.
 *
 * @see [NavigationControllerBuilder]
 *
 * @return [NavigationController]
 */
fun navigationController(
    eventBus: EventBus,
    defaultRoute: String,
    block: NavigationControllerBuilder.() -> Unit
) =
    NavigationControllerBuilder(eventBus, defaultRoute).apply(block).build()

/**
 * Creates navigation controller in composable functions and remembers it.
 * Should be invoked on every Activity creation, because
 * [NavigationEntry][com.varpihovsky.core_nav.main.NavigationEntry] can't be parcelized.
 * Backstack is saved. You haven't to restore it manually.
 *
 * @see [NavigationControllerBuilder]
 *
 * @return [NavigationController]
 */
@Composable
fun rememberNavigationController(
    eventBus: EventBus,
    defaultRoute: String,
    block: NavigationControllerBuilder.() -> Unit
) = rememberSaveable(saver = navigationControllerSaver(eventBus)) {
    NavigationControllerBuilder(eventBus, defaultRoute).apply(block).build()
}

@Composable
private fun navigationControllerSaver(eventBus: EventBus): Saver<NavigationController, *> = Saver(
    save = { it.saveState() },
    restore = { NavigationController(eventBus, listOf(), "").apply { restoreState(it) } }
)

/**
 * Receives events from navigation controller and shows current composable.
 * If received finish event finishing current activity.
 *
 * @see [navigationController]
 * @see [rememberNavigationController]
 */
@Composable
fun DisplayNavigation(
    modifier: Modifier = Modifier,
    controller: NavigationController
) {
    val current = controller.operation.collectAsState(NavigationOperation.Navigate()).value
    var screen: NavigationOperation.Navigate = NavigationOperation.Navigate()

    when (current) {
        is NavigationOperation.Finish -> current.process {
            (LocalContext.current as Activity).finish()
        }

        is NavigationOperation.Navigate -> screen = current
    }

    MaterialMotion(
        modifier = modifier,
        targetState = screen.route,
        motionSpec = screen.motionSpec
    ) { str ->
        controller.entries.find { it.route == str }?.composable?.invoke()
    }
}