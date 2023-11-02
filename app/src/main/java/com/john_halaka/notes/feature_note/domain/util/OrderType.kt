package com.john_halaka.notes.feature_note.domain.util

sealed class OrderType {

    object Ascending : OrderType() {
        override fun toString() = "Ascending"
    }

    object Descending : OrderType() {
        override fun toString() = "Descending"
    }

    companion object {
        fun fromString(orderTypeString: String): OrderType {
            return when (orderTypeString) {
                "Ascending" -> Ascending
                "Descending" -> Descending
                else -> throw IllegalArgumentException("Unknown order type string")
            }
        }
    }
}
