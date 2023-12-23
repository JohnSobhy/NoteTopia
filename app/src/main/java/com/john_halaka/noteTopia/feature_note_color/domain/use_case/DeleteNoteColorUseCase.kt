package com.john_halaka.noteTopia.feature_note_color.domain.use_case

import com.john_halaka.noteTopia.feature_note_color.domain.model.NoteColor
import com.john_halaka.noteTopia.feature_note_color.domain.repository.NoteColorRepository

class DeleteNoteColorUseCase(private val noteColorRepository: NoteColorRepository) {
    suspend operator fun invoke(noteColor: NoteColor) {
        noteColorRepository.delete(noteColor)
    }
}