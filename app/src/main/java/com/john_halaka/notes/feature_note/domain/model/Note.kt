package com.john_halaka.notes.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.john_halaka.notes.ui.theme.Gray
import com.john_halaka.notes.ui.theme.LightGreen
import com.john_halaka.notes.ui.theme.MellonRed
import com.john_halaka.notes.ui.theme.SandyBrown
import com.john_halaka.notes.ui.theme.VistaBlue

@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val isFavourite: Boolean = false,
    val isDeleted: Boolean = false
){
    companion object {
        val noteColors = listOf(SandyBrown, VistaBlue, MellonRed, LightGreen, Gray)
        val noteCategory = listOf("Work", "Personal", "Family", "Shopping")
    }


}

class InvalidNoteException(message: String) : Exception(message)
