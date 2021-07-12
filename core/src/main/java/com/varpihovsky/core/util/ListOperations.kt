package com.varpihovsky.core.util

fun <T> List<T>.remove(t: T): List<T> {
    val mutable = mutableListOf<T>().apply { addAll(this@remove) }
    mutable.remove(t)
    return mutable
}