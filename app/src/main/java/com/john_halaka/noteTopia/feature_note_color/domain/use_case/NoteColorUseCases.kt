package com.john_halaka.noteTopia.feature_note_color.domain.use_case

data class NoteColorUseCases(
    val addColorUseCase: AddColorUseCase,
    val getNoteColorsUseCase: GetNoteColorsUseCase,
    val deleteAllColorsUseCase: DeleteAllColorsUseCase,
    val deleteNoteColorUseCase: DeleteNoteColorUseCase,
    val colorExistsUseCase: ColorExistsUseCase
)
