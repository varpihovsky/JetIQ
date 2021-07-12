package com.varpihovsky.ui_data.func_extensions

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class Selectable<T>(val dto: @RawValue T, val isSelected: Boolean) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Selectable<*>

        if (dto != other.dto) return false
        if (isSelected != other.isSelected) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dto?.hashCode() ?: 0
        result = 31 * result + isSelected.hashCode()
        return result
    }

    fun selectedToggle() = Selectable(dto, !isSelected)
}