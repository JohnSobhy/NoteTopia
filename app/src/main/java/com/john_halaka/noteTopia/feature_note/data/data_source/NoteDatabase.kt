package com.john_halaka.noteTopia.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_todo.data.data_source.TodoDao
import com.john_halaka.noteTopia.feature_todo.domain.model.Todo


@Database(
    entities = [Note::class, Todo::class],
    version = 2
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
    abstract val todoDao: TodoDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}