package com.john_halaka.noteTopia.feature_note.domain

import com.john_halaka.noteTopia.ui.presentaion.add_edit_note.NoteTextFieldState

data class NoteContentState(
    val noteId: Int = -1,
    val noteTitle: NoteTextFieldState = NoteTextFieldState(
        hint = "Enter note title"
    ),
    val noteContent: NoteTextFieldState = NoteTextFieldState(
        hint = "Enter note content"
    ),
    val noteColor: Int = -1,
    val noteIsFavorite: Boolean = false,
    val noteIsDeleted: Boolean = false,
    val noteIsPinned: Boolean = false,
    val timestamp: Long = -1L
)
