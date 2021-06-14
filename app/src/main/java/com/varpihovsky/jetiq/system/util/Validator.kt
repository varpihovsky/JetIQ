package com.varpihovsky.jetiq.system.util

interface Validator<T> {
    fun validate(t: T): Boolean
}