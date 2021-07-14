package com.varpihovsky.core_nav.main

sealed class EntryType {
    object Main : EntryType()
    object SubMenu : EntryType()
}