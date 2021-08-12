package com.varpihovsky.core_nav.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

actual sealed class EntryType {
    @Parcelize
    actual object Main : EntryType(), Parcelable

    @Parcelize
    actual object SubMenu : EntryType(), Parcelable
}