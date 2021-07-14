package com.varpihovsky.core_nav.dsl

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.varpihovsky.core_nav.main.NavigationController
import com.varpihovsky.core_nav.main.NavigationOperation
import com.varpihovsky.core_nav.main.OperationType
import soup.compose.material.motion.MaterialMotion

fun navigationController(defaultRoute: String, block: NavigationControllerBuilder.() -> Unit) =
    NavigationControllerBuilder(defaultRoute).apply(block).build()

@Composable
fun DisplayNavigation(modifier: Modifier = Modifier, controller: NavigationController) {
    val current = controller.operation.collectAsState(NavigationOperation()).value

    if (current.type == OperationType.FINISH) {
        (LocalContext.current as Activity).finish()
    }

    MaterialMotion(
        modifier = modifier,
        targetState = current.route,
        motionSpec = current.motionSpec
    ) { str ->
        controller.entries.find { it.route == str }?.composable?.invoke()
    }
}