package com.varpihovsky.core.util

fun <T> List<T>.replaceAndReturn(from: T, to: T): List<T> {
    val mutable = toMutableList()
    val fromIndex = mutable.indexOf(from)
    mutable.removeAt(fromIndex)
    mutable.add(fromIndex, to)
    return mutable
}

fun <T> List<T>.remove(t: T): List<T> {
    val mutable = toMutableList()
    mutable.remove(t)
    return mutable
}

fun <T> List<T>.removeLastAndReturn(): List<T> {
    val mutable = toMutableList()
    mutable.removeLast()
    return mutable
}

fun <T> List<T>.addAndReturn(element: T): List<T> {
    val mutable = toMutableList()
    mutable.add(element)
    return mutable
}

fun <T> List<Selectable<T>>.selectedOnly(): List<T> {
    return filter { it.isSelected }.map { it.dto }
}
