package com.john_halaka.notes.feature_note.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.john_halaka.notes.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {

    @Query("SELECT * FROM note WHERE isDeleted = 0")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE isFavourite = 1")
    fun getFavouriteNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE isDeleted = 1")
    fun getDeletedNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("UPDATE note SET isFavourite = :isFavourite WHERE id = :noteId")
    suspend fun updateIsFavourite(noteId: Int, isFavourite: Boolean)

    @Query("UPDATE note SET isDeleted = :isDeleted WHERE id = :noteId")
    suspend fun updateIsDeleted(noteId: Int, isDeleted: Boolean)
}