package com.john_halaka.notes.feature_note.data.repository

import com.john_halaka.notes.feature_note.data.data_source.NoteDao
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl (
    private val dao: NoteDao
        ): NoteRepository {
    // as we only have one source of data which is local database
    // so we only implement these functions from the dao
    // if we also have an API for example, then we will add more logic to these functions
    // to determine which source we should get the data from
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}