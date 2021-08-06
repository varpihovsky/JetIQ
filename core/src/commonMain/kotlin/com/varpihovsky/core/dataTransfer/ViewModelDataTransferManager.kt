package com.varpihovsky.core.dataTransfer

/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Due to SharedViewModel pattern is hard to use with jetpack compose, in every ViewModel communication
 * you have to use this class.
 *
 * @author Vladyslav Podrezenko
 */
class ViewModelDataTransferManager {
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