package com.varpihovsky.core.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Class that used for simplifying selection process. To get data out of it use [state] value.
 *
 * @param dataSource used to put data inside of it.
 * @param scope used to map lifecycle with ViewModel.
 * @param dispatcher if you need to specify dispatcher.
 *
 * @author Vladyslav Podrezenko
 */
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

    /**
     * Looks for [Selectable] with same dto and changes [Selectable.isSelected] to opposite.
     * Than puts it into [state].
     */
    fun toggle(element: T) {
        _state.value.find { it.dto == element }?.let { toggle(it) }
    }

    /**
     * Toggles same element with same [Selectable.isSelected] field value and changes [Selectable.isSelected]
     * to opposite. Than puts it into [state].
     */
    fun toggle(element: Selectable<T>) {
        _state.value = _state.value.replaceAndReturn(element, element.selectedToggle())
    }

    /**
     * Deselects every selected element and puts it into [state].
     */
    fun deselectAll() {
        _state.value = _state.value.map { Selectable(it.dto, false) }
    }

    /**
     * Returns true if there is any selected [Selectable].
     */
    fun isAnySelected() = _state.value.any { it.isSelected }
}