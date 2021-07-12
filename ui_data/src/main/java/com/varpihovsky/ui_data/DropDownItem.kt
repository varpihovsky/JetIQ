package com.varpihovsky.ui_data

interface DropDownItem {
    val text: String
}

class EmptyDropDownItem : DropDownItem {
    override val text: String
        get() = ""

}

class IdDropDownItem(val id: Int, override val text: String) : DropDownItem

enum class ContactTypeDropDownItem : DropDownItem {
    STUDENT {
        override val text: String
            get() = "Студент"
    },
    TEACHER {
        override val text: String
            get() = "Викладач"

    }
}