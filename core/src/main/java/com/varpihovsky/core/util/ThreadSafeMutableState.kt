package com.varpihovsky.core.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

class ThreadSafeMutableState<V>(value: V, private val scope: CoroutineScope) :
    MutableState<V> {
    private val state = mutableStateOf(value)

    override fun component1(): V {
        return state.component1()
    }

    override fun component2(): (V) -> Unit {
        return state.component2()
    }

    override var value: V
        get() = state.value
        set(value) {
            scope.launch(Dispatchers.Main) {
                state.value = value
            }
        }

    operator fun <R> setValue(
        thisRef: R,
        property: KProperty<*>,
        value: V
    ) {
        this.value = value
    }
}