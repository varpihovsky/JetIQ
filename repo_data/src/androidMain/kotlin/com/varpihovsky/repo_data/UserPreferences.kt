package com.varpihovsky.repo_data

actual enum class SubjectListType {
    FULL {
        actual override fun toString(): String = FULL_STRING

    },
    PARTIAL {
        actual override fun toString(): String = PARTIAL_STRING
    };

    actual abstract override fun toString(): String

    actual companion object {
        private const val FULL_STRING = "Повний"
        private const val PARTIAL_STRING = "По семестру"

        actual fun ofName(name: String?) = valueOf(name ?: PARTIAL.name)

        actual fun ofString(string: String) = when (string) {
            FULL_STRING -> FULL
            PARTIAL_STRING -> PARTIAL
            else -> throw IllegalStateException("Wrong string!")
        }
    }
}

actual enum class ExpandButtonLocation {
    LOWER {
        actual override fun toString(): String = LOWER_STRING
    },
    UPPER {
        actual override fun toString(): String = UPPER_STRING

    };

    actual abstract override fun toString(): String

    actual companion object {
        private const val UPPER_STRING = "Над списком"
        private const val LOWER_STRING = "Під списком"

        actual fun ofName(name: String?) = valueOf(name ?: LOWER.name)

        actual fun ofString(string: String) = when (string) {
            LOWER_STRING -> LOWER
            UPPER_STRING -> UPPER
            else -> throw IllegalStateException("Wrong string!")
        }
    }
}