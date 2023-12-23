package com.john_halaka.noteTopia.feature_note_color.data.repository

import com.john_halaka.noteTopia.feature_note_color.data.data_source.NoteColorDao
import com.john_halaka.noteTopia.feature_note_color.domain.model.NoteColor
import com.john_halaka.noteTopia.feature_note_color.domain.repository.NoteColorRepository

class NoteColorRepositoryImpl(private val dao: NoteColorDao) : NoteColorRepository {
    override suspend fun getAll(): List<NoteColor> {
        return dao.getAll()
    }

    override suspend fun insert(noteColor: NoteColor) {
        dao.insert(noteColor)
    }

    override suspend fun delete(noteColor: NoteColor) {
        dao.delete(noteColor)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun colorExists(argb: Int): Int {
        return dao.colorExists(argb)
    }
}