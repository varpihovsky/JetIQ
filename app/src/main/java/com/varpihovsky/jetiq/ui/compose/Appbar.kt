package com.varpihovsky.jetiq.ui.compose

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.varpihovsky.core.appbar.AppbarCommand
import com.varpihovsky.core.appbar.AppbarManager
import soup.compose.material.motion.Axis
import soup.compose.material.motion.MaterialSharedAxis

@ExperimentalAnimationApi
@Composable
fun Appbar(appbarManager: AppbarManager) {
    val current = appbarManager.commands.collectAsState(initial = AppbarCommand.Empty).value
    val isShown = remember { mutableStateOf(true) }

    val title = remember { mutableStateOf<String?>(null) }
    val navIcon = remember { mutableStateOf<(@Composable () -> Unit)?>(null) }
    val actions = remember { mutableStateOf<(@Composable RowScope.() -> Unit)?>(null) }
    val bar = remember { mutableStateOf<(@Composable () -> Unit)?>(null) }

    when (current) {
        is AppbarCommand.Custom -> {
            isShown.value = true
            bar.value = current.bar
        }
        AppbarCommand.Empty -> isShown.value = false

        is AppbarCommand.Configured -> {
            bar.value = null
            isShown.value = true
            title.value = current.title
            navIcon.value = current.navIcon
            actions.value = current.actions
        }
    }

    AnimatedVisibility(
        visible = isShown.value,
        enter = expandVertically(expandFrom = Alignment.CenterVertically, clip = false),
        exit = shrinkVertically(shrinkTowards = Alignment.CenterVertically)
    ) {
        bar.value?.invoke() ?: TopAppBar(
            title = {
                MaterialSharedAxis(
                    targetState = title.value,
                    axis = Axis.Y,
                    forward = true
                ) {
                    it?.let { Text(text = it) }
                }
            },
            navigationIcon = navIcon.value,
            actions = {
                AnimatedVisibility(
                    modifier = Modifier.animateContentSize(),
                    visible = actions.value != null,
                ) {
                    Row {
                        actions.value?.invoke(this)
                    }
                }
            }
        )
    }
}