package com.varpihovsky.core_lifecycle

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

interface BottomBarController {
    fun select(bottomBarEntry: BottomBarEntry)

    fun hide()

    fun show()
}

sealed class BottomBarEntry : Parcelable {
    @Parcelize
    object Profile : BottomBarEntry()

    @Parcelize
    object Messages : BottomBarEntry()
}