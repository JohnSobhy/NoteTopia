package com.john_halaka.noteTopia.feature_note_color.domain.repository

import com.john_halaka.noteTopia.feature_note_color.domain.model.NoteColor

interface NoteColorRepository {
    suspend fun getAll(): List<NoteColor>
    suspend fun insert(noteColor: NoteColor)
    suspend fun delete(noteColor: NoteColor)
    suspend fun deleteAll()
    suspend fun colorExists(argb: Int): Int
}