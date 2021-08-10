package com.varpihovsky.core_ui.compose.entities

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.varpihovsky.core_ui.R

@Composable
internal actual fun checkIcon(): Painter = painterResource(R.drawable.ic_baseline_check_24)