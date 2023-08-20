package com.john_halaka.notes.feature_note.domain.use_case

import com.john_halaka.notes.feature_note.domain.repository.NoteRepository

class MoveNoteToTrash(private val repository: NoteRepository) {

    suspend operator fun invoke(noteId: Int, isDeleted: Boolean) {
        repository.moveNoteToTrash(noteId, isDeleted)
    }
}