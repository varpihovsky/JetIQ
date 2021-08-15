package com.varpihovsky.core_db.internal

import com.varpihovsky.core.log.v
import com.varpihovsky.core.util.add
import com.varpihovsky.core.util.remove
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.kodein.db.DBListener
import org.kodein.db.Operation
import org.kodein.db.model.orm.Metadata

class DataFetcher<T : Metadata>(initData: List<T>) : DBListener<T> {
    private val _flow = MutableStateFlow(listOf<T>())
    val flow: StateFlow<List<T>>
        get() = _flow


    init {
        _flow.value = initData
    }

    override fun didDelete(operation: Operation.Delete<T>) {
        operation.model()?.let {
            _flow.value = _flow.value.remove(it)
        }
    }

    override fun didPut(operation: Operation.Put<T>) {
        if (_flow.value.find { it.id == operation.model.id } == null) {
            v(operation.model.toString())
            _flow.value = _flow.value.add(operation.model)
        }
    }
}