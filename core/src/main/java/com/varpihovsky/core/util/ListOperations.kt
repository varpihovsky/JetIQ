package com.varpihovsky.core.util

/**
 * Returns same list but instead of same as "from" element, has "to" element.
 */
fun <T> List<T>.replaceAndReturn(from: T, to: T): List<T> {
    val mutable = toMutableList()
    val fromIndex = mutable.indexOf(from)
    mutable.removeAt(fromIndex)
    mutable.add(fromIndex, to)
    return mutable
}

/**
 * Returns same list but without specified element.
 */
fun <T> List<T>.remove(t: T): List<T> {
    val mutable = toMutableList()
    mutable.remove(t)
    return mutable
}

/**
 * Returns only selected and mapped to dto list.
 */
fun <T> List<Selectable<T>>.selectedOnly(): List<T> {
    return filter { it.isSelected }.map { it.dto }
}
