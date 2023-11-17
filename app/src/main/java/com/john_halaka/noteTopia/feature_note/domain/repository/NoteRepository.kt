package com.john_halaka.noteTopia.feature_note.domain.repository

import com.john_halaka.noteTopia.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    fun getFavouriteNotes(): Flow<List<Note>>

    fun getDeletedNotes(): Flow<List<Note>>

    suspend fun updateNote(noteId: Int, isFavourite: Boolean)

    suspend fun moveNoteToTrash(noteId: Int, isDeleted: Boolean)

}