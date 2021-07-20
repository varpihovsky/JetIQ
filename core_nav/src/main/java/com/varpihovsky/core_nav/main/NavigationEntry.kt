package com.varpihovsky.core_nav.main

import android.os.Parcelable
import androidx.compose.runtime.Composable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import soup.compose.material.motion.MotionSpec

/**
 * Class that stores data about entry.
 *
 * @author Vladyslav Podrezenko
 */
@Parcelize
class NavigationEntry(
    /**
     * Specifies composable of entry. On one controller can be shown only one composable.
     */
    val composable: @Composable () -> Unit,

    /**
     * Specifies route of entry.
     */
    val route: String,

    /**
     * Specifies type of entry.
     *
     * @see EntryType
     */
    val type: @RawValue EntryType,

    /**
     * Specifies animation that will be played when composable appears.
     */
    val inAnimation: @RawValue MotionSpec,

    /**
     * Specifies animation that will be played when composable removed from stack.
     */
    val outAnimation: @RawValue MotionSpec
) : Parcelable

