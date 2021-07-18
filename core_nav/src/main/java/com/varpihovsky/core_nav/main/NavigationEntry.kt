package com.varpihovsky.core_nav.main

import android.os.Parcelable
import androidx.compose.runtime.Composable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import soup.compose.material.motion.MotionSpec

@Parcelize
class NavigationEntry(
    val composable: @Composable () -> Unit,
    val route: String,
    val type: @RawValue EntryType,
    val inAnimation: @RawValue MotionSpec,
    val outAnimation: @RawValue MotionSpec
) : Parcelable

