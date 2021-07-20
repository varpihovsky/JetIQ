package com.varpihovsky.core_nav.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Type of current entry. From entry type depends behavior of [NavigationController.manage] command.
 * When entry is Main it deletes current navigation stack and pushes it to the top.
 * Wen entry is SubMenu it pushes it to the top of the stack without deleting.
 *
 * @see [NavigationController]
 *
 * @author Vladyslav Podrezenko
 */
sealed class EntryType {
    @Parcelize
    object Main : EntryType(), Parcelable

    @Parcelize
    object SubMenu : EntryType(), Parcelable
}