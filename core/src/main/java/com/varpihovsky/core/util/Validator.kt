package com.varpihovsky.core.util

@FunctionalInterface
interface Validator<T> {
    fun validate(t: T): Boolean
}