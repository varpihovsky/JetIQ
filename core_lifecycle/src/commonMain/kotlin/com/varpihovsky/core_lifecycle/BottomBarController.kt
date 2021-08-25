package com.varpihovsky.core_lifecycle

interface BottomBarController {
    fun select(bottomBarEntry: BottomBarEntry)

    fun hide()

    fun show()
}

sealed class BottomBarEntry {
    object Profile : BottomBarEntry()
    object Messages : BottomBarEntry()
}