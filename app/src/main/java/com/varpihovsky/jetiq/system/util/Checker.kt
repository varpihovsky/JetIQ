package com.varpihovsky.jetiq.system.util

interface Checker<T> {
    fun check(t: T): Boolean
}