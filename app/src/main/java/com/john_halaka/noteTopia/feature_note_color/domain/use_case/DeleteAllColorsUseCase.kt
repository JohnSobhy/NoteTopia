package com.john_halaka.noteTopia.feature_note_color.domain.use_case

import com.john_halaka.noteTopia.feature_note_color.domain.repository.NoteColorRepository

class DeleteAllColorsUseCase(private val noteColorRepository: NoteColorRepository) {
    suspend operator fun invoke() {
        noteColorRepository.deleteAll()
    }
}