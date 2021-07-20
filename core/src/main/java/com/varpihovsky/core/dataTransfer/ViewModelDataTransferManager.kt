package com.varpihovsky.core.dataTransfer

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * Due to SharedViewModel pattern is hard to use with jetpack compose, in every ViewModel communication
 * you have to use this class.
 *
 * @author Vladyslav Podrezenko
 */
class ViewModelDataTransferManager @Inject constructor() {
    private val flows = hashMapOf<String, MutableStateFlow<ViewModelData<*>?>>()

    /**
     * Returns unique flow by unique tag.
     */
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