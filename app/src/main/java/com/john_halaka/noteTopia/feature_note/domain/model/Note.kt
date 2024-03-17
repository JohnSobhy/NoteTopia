package com.john_halaka.noteTopia.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.john_halaka.noteTopia.ui.theme.DarkTurquoise
import com.john_halaka.noteTopia.ui.theme.LightGreen
import com.john_halaka.noteTopia.ui.theme.Purple80
import com.john_halaka.noteTopia.ui.theme.SandyBrown
import com.john_halaka.noteTopia.ui.theme.VistaBlue

@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val isFavourite: Boolean = false,
    val isDeleted: Boolean = false,
    val isPinned: Boolean = false
) {
    companion object {
        val noteColors = listOf(SandyBrown, VistaBlue, DarkTurquoise, LightGreen, Purple80)
        // val noteCategory = listOf("Work", "Personal", "Family", "Shopping")
    }
}
class InvalidNoteException(message: String) : Exception(message)
