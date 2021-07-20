package com.varpihovsky.core.util

@FunctionalInterface
interface Validator<T> {
    /**
     * Should return true if parameter passes checking.
     */
    fun validate(t: T): Boolean
}