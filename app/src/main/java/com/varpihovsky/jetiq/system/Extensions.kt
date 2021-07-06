package com.varpihovsky.jetiq.system

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.resetValue(value: T) {
    this.value = null
    this.value = value
}