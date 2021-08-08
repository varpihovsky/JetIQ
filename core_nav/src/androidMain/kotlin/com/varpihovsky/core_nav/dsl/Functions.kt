package com.varpihovsky.core_nav.dsl

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun finish() {
    (LocalContext.current as Activity).finish()
}