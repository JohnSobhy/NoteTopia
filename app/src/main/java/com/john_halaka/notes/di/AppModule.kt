package com.john_halaka.notes.di

import android.app.Application
import androidx.room.Room


import com.john_halaka.notes.feature_note.data.data_source.NoteDatabase
import com.john_halaka.notes.feature_note.data.repository.NoteRepositoryImpl
import com.john_halaka.notes.feature_note.domain.repository.NoteRepository
import com.john_halaka.notes.feature_note.domain.use_case.AddNote
import com.john_halaka.notes.feature_note.domain.use_case.DeleteNote
import com.john_halaka.notes.feature_note.domain.use_case.GetFavouriteNotes
import com.john_halaka.notes.feature_note.domain.use_case.GetNoteById
import com.john_halaka.notes.feature_note.domain.use_case.GetNotes
import com.john_halaka.notes.feature_note.domain.use_case.NoteUseCases
import com.john_halaka.notes.feature_note.domain.use_case.UpdateNote
import com.john_halaka.notes.feature_todo.data.repository.TodoRepositoryImpl
import com.john_halaka.notes.feature_todo.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase (app: Application) : NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ) .build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository (db: NoteDatabase) : NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases (repository: NoteRepository) : NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNotes = DeleteNote(repository),
            addNote = AddNote(repository),
            getNoteById = GetNoteById(repository),
            getFavouriteNotes = GetFavouriteNotes(repository),
            updateNote = UpdateNote(repository)

        )
    }



    @Provides
    @Singleton
    fun provideTodoRepository (db: NoteDatabase) : TodoRepository {
        return TodoRepositoryImpl(db.todoDao)
    }


}