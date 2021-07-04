package com.varpihovsky.jetiq.ui.dto.func_extensions

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class Selectable<T>(val dto: @RawValue T, val isSelected: Boolean) : Parcelable {
    fun selectedToggle() = Selectable(dto, !isSelected)
}