package com.varpihovsky.core_nav.dsl

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