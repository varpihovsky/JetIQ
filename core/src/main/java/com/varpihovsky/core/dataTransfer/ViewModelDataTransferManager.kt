package com.varpihovsky.core.dataTransfer

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ViewModelDataTransferManager @Inject constructor() {
    private val flows = hashMapOf<String, MutableStateFlow<ViewModelData<*>?>>()

    fun getFlowByTag(tag: String) =
        flows[tag].let {
            val flow: MutableStateFlow<ViewModelData<*>?>
            if (it == null) {
                flow = MutableStateFlow(null)
                flows[tag] = flow
            } else {
                flow = it
            }
            flow
        }
}