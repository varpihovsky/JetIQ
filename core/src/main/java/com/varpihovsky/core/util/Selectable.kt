package com.varpihovsky.core.util

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * Wrapper class used for mark selection.
 *
 * @author Vladyslav Podrezenko
 */
@Parcelize
data class Selectable<T>(val dto: @RawValue T, val isSelected: Boolean) : Parcelable {

    /**
     * Returns same instance but with opposite [isSelected] field.
     *
     * @return [Selectable]
     */
    fun selectedToggle() = Selectable(dto, !isSelected)
}