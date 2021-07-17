package com.varpihovsky.core.util

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Selectable<T>(val dto: @RawValue T, val isSelected: Boolean) : Parcelable {
    fun selectedToggle() = Selectable(dto, !isSelected)
}