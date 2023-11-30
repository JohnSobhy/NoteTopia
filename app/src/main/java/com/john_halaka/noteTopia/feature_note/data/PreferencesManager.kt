package com.john_halaka.noteTopia.feature_note.data

import android.content.Context
import android.content.SharedPreferences
import com.john_halaka.noteTopia.feature_note.domain.util.NoteOrder
import com.john_halaka.noteTopia.feature_note.domain.util.OrderType

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveNoteOrder(noteOrder: NoteOrder) {
        val editor = sharedPreferences.edit()
        editor.putString("note_order", noteOrder.toString())
        editor.putString("order_type", noteOrder.orderType.toString())
        editor.apply()
    }

    // Retrieve the user's preference
    fun getNoteOrder(): NoteOrder {
        val orderTypeString = getString("order_type", "Descending")
        val noteOrderString = getString("note_order", "Date")

        val orderType = OrderType.fromString(orderTypeString)
        return NoteOrder.fromString(noteOrderString, orderType)
    }

//    fun getNoteOrder(): Comparator<Note> {
//        val orderTypeString = getString("order_type", "Descending")
//        val noteOrderString = getString("note_order", "Date")
//
//        val orderType = OrderType.fromString(orderTypeString)
//        val noteOrder = NoteOrder.fromString(noteOrderString, orderType)
//
//        return noteOrder.comparator()
//    }


}
