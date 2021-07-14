package com.varpihovsky.core_nav.main

sealed class OperationType {
    object Navigate : OperationType()
    object FINISH : OperationType()
}
