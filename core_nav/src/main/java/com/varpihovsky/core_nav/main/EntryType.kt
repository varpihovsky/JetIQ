package com.varpihovsky.core_nav.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class EntryType {
    @Parcelize
    object Main : EntryType(), Parcelable

    @Parcelize
    object SubMenu : EntryType(), Parcelable
}