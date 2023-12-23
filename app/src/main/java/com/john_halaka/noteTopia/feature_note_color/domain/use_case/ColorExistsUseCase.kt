package com.john_halaka.noteTopia.feature_note_color.domain.use_case

import com.john_halaka.noteTopia.feature_note_color.domain.repository.NoteColorRepository

class ColorExistsUseCase(private val noteColorRepository: NoteColorRepository) {
    suspend operator fun invoke(argb: Int): Int {
        return noteColorRepository.colorExists(argb)
    }
}
