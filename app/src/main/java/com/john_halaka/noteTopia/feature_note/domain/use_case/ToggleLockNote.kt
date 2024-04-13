package com.john_halaka.noteTopia.feature_note.domain.use_case

import com.john_halaka.noteTopia.feature_note.domain.repository.NoteRepository

class ToggleLockNote(private val repository: NoteRepository) {
    suspend operator fun invoke(noteId: Int, isLocked: Boolean) {
        repository.lockNote(noteId, isLocked)
    }
}