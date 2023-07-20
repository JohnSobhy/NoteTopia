package com.john_halaka.notes.feature_note.domain.util

sealed class OrderType {

    object Ascending : OrderType ()
    object Descending : OrderType ()

}
