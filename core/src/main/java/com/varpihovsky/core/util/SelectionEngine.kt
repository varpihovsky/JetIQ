package com.varpihovsky.core.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SelectionEngine<T>(
    dataSource: Flow<List<T>>,
    scope: CoroutineScope,
    dispatcher: CoroutineDispatcher
) {
    val state: StateFlow<List<Selectable<T>>>
        get() = _state

    private val _state = MutableStateFlow(listOf<Selectable<T>>())

    init {
        scope.launch(dispatcher) {
            dataSource.collect { synchronizeChanges(it) }
        }
    }

    private fun synchronizeChanges(changes: List<T>) {
        // Add added and remove removed
        _state.value = _state.value.filter { !changes.contains(it.dto) } + changes.map {
            Selectable(it, false)
        }
    }

    fun toggle(element: T) {
        _state.value.find { it.dto == element }?.let { toggle(it) }
    }

    fun toggle(element: Selectable<T>) {
        _state.value = _state.value.replaceAndReturn(element, element.selectedToggle())
    }

    fun deselectAll() {
        _state.value = _state.value.map { Selectable(it.dto, false) }
    }

    fun isAnySelected() = _state.value.any { it.isSelected }
}