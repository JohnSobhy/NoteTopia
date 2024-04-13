package com.john_halaka.noteTopia.feature_note.data.repository

import android.util.Log
import com.john_halaka.noteTopia.feature_note.data.data_source.NoteDao
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val dao: NoteDao) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        Log.d("repository", "getNotes from repositoryImp")
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

    override fun getFavouriteNotes(): Flow<List<Note>> {
        return dao.getFavouriteNotes()
    }

    override fun getLockedNotes(): Flow<List<Note>> {
        return dao.getLockedNotes()
    }

    override fun getDeletedNotes(): Flow<List<Note>> {
        return dao.getDeletedNotes()
    }


    override suspend fun updateNote(noteId: Int, isFavourite: Boolean) {
        return dao.updateIsFavourite(noteId, isFavourite)
    }

    override suspend fun moveNoteToTrash(noteId: Int, isDeleted: Boolean) {
        return dao.updateIsDeleted(noteId, isDeleted)
    }

    override suspend fun pinNote(noteId: Int, isPinned: Boolean) {
        return dao.updateIsPinned(noteId, isPinned)
    }

    override suspend fun lockNote(noteId: Int, isLocked: Boolean) {
        return dao.updateIsLocked(noteId, isLocked)
    }
}