package com.john_halaka.notes.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.john_halaka.notes.ui.theme.Gray
import com.john_halaka.notes.ui.theme.LightBlue
import com.john_halaka.notes.ui.theme.LightGreen
import com.john_halaka.notes.ui.theme.LightPink
import com.john_halaka.notes.ui.theme.LightViolet
import com.john_halaka.notes.ui.theme.MellonRed
import com.john_halaka.notes.ui.theme.White

@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val isFavourite: Boolean = false
){
    companion object {
        val noteColors = listOf(White, LightBlue, LightPink, LightViolet, LightGreen, Gray,  MellonRed)
    }
}

class InvalidNoteException(message: String) : Exception(message)
