package com.john_halaka.noteTopia.feature_note.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.john_halaka.noteTopia.feature_note.domain.util.NoteOrder
import com.john_halaka.noteTopia.feature_note.domain.util.OrderType

class PreferencesManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences =
        EncryptedSharedPreferences.create(
            context,
            "MyPrefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    //  context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

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

}
