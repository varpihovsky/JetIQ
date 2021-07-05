package com.varpihovsky.jetiq.system.navigation

import androidx.annotation.DrawableRes
import com.varpihovsky.jetiq.R

sealed class BottomNavigationItem(
    val route: NavigationCommand,
    val subscription: String,
    @DrawableRes val iconId: Int
) {
    object ProfileItem : BottomNavigationItem(
        NavigationDirections.profile,
        "Профіль",
        R.drawable.ic_baseline_person_24
    )

    object MessagesItem : BottomNavigationItem(
        NavigationDirections.messages,
        "Повідомлення",
        R.drawable.ic_baseline_message_24
    )
}
