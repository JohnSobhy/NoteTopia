package com.john_halaka.noteTopia.feature_note.domain.util

sealed class NoteOrder(val orderType: OrderType) {

    class Title(orderType: OrderType) : NoteOrder(orderType) {
        override fun toString() = "Title"
    }

    class Date(orderType: OrderType) : NoteOrder(orderType) {
        override fun toString() = "Date"
    }

    class Color(orderType: OrderType) : NoteOrder(orderType) {
        override fun toString() = "Color"
    }
    // abstract fun comparator(): Comparator<Note>

    companion object {
        fun fromString(orderString: String, orderType: OrderType): NoteOrder {
            return when (orderString) {
                "Title" -> Title(orderType)
                "Date" -> Date(orderType)
                "Color" -> Color(orderType)
                else -> throw IllegalArgumentException("Unknown order string")
            }
        }
    }

    fun copy(orderType: OrderType): NoteOrder {
        return when (this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
            is Color -> Color(orderType)
        }
    }

}
