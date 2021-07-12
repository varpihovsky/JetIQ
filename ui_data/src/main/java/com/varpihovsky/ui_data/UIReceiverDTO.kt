package com.varpihovsky.ui_data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UIReceiverDTO(val id: Int, val text: String, val type: ReceiverType) : Parcelable {
    fun getPhotoURL() = type.getPhotoMethodURL() + id
}

enum class ReceiverType {
    STUDENT {
        override fun getPhotoMethodURL(): String =
            "https://iq.vntu.edu.ua/b06175/getfile.php?stud_id="

        override fun toInt() = 0
    },
    TEACHER {
        override fun getPhotoMethodURL(): String =
            "https://iq.vntu.edu.ua/method/get_photo.php?id="

        override fun toInt(): Int = 1
    };


    abstract fun getPhotoMethodURL(): String

    abstract fun toInt(): Int
}
