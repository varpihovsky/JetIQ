package com.varpihovsky.core_nav.main

class NavigationControllerStorage {
    var navigationController: NavigationController
        get() = checkNotNull(_navigationController)
        set(value) {
            _navigationController = value
        }

    private var _navigationController: NavigationController? = null
}