package com.varpihovsky.jetiq.system.util

@FunctionalInterface
interface Validator<T> {
    fun validate(t: T): Boolean
}