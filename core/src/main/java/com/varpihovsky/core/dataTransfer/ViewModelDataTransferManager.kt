package com.varpihovsky.core.dataTransfer

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ViewModelDataTransferManager @Inject constructor() {
    private val flows = hashMapOf<String, MutableStateFlow<ViewModelData<*>?>>()

    fun getFlowByTag(tag: String) = createIfNotExists(flows[tag], tag)

    private fun createIfNotExists(
        flow: MutableStateFlow<ViewModelData<*>?>?,
        tag: String
    ): MutableStateFlow<ViewModelData<*>?> {
        if (flow == null) {
            return MutableStateFlow<ViewModelData<*>?>(null).also {
                flows[tag] = it
            }
        }

        return flow
    }
}