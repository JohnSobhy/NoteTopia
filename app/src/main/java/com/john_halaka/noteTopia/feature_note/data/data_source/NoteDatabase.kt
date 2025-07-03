package com.john_halaka.noteTopia.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_note_color.data.data_source.NoteColorDao
import com.john_halaka.noteTopia.feature_note_color.domain.model.NoteColor
import com.john_halaka.noteTopia.feature_todo.data.data_source.TodoDao
import com.john_halaka.noteTopia.feature_todo.domain.model.Todo


@Database(
    entities = [Note::class, Todo::class, NoteColor::class],
    version = 6,
    exportSchema = true,
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
    abstract val todoDao: TodoDao
    abstract val noteColorDao: NoteColorDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}