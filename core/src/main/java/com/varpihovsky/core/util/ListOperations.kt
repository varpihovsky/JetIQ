package com.varpihovsky.core.util

fun <T> List<T>.remove(t: T): List<T> {
    val mutable = toMutableList()
    mutable.remove(t)
    return mutable.toList()
}

fun <T> List<T>.removeLastAndReturn(): List<T> {
    val mutable = toMutableList()
    mutable.removeLast()
    return mutable.toList()
}

fun <T> List<T>.addAndReturn(element: T): List<T> {
    val mutable = toMutableList()
    mutable.add(element)
    return mutable.toList()
}