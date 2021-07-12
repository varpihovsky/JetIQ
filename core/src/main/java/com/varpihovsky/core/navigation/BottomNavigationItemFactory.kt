package com.varpihovsky.core.navigation

object BottomNavigationItemFactory {
    fun create(navigationCommand: NavigationCommand) = when (navigationCommand) {
        NavigationDirections.profile -> BottomNavigationItem.ProfileItem
        NavigationDirections.messages -> BottomNavigationItem.MessagesItem
        else -> null
    }
}