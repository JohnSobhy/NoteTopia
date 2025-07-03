package com.john_halaka.noteTopia.di


import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.john_halaka.noteTopia.feature_daily_quote.data.repository.QuoteRepositoryImpl
import com.john_halaka.noteTopia.feature_daily_quote.domain.repository.QuoteRepository
import com.john_halaka.noteTopia.feature_note.data.PreferencesManager
import com.john_halaka.noteTopia.feature_note.data.data_source.NoteDatabase
import com.john_halaka.noteTopia.feature_note.data.repository.NoteRepositoryImpl
import com.john_halaka.noteTopia.feature_note.domain.repository.NoteRepository
import com.john_halaka.noteTopia.feature_note.domain.use_case.AddNote
import com.john_halaka.noteTopia.feature_note.domain.use_case.DeleteNote
import com.john_halaka.noteTopia.feature_note.domain.use_case.GetDeletedNotes
import com.john_halaka.noteTopia.feature_note.domain.use_case.GetFavouriteNotes
import com.john_halaka.noteTopia.feature_note.domain.use_case.GetLockedNotes
import com.john_halaka.noteTopia.feature_note.domain.use_case.GetNoteById
import com.john_halaka.noteTopia.feature_note.domain.use_case.GetNotes
import com.john_halaka.noteTopia.feature_note.domain.use_case.MoveNoteToTrash
import com.john_halaka.noteTopia.feature_note.domain.use_case.NoteUseCases
import com.john_halaka.noteTopia.feature_note.domain.use_case.ToggleLockNote
import com.john_halaka.noteTopia.feature_note.domain.use_case.TogglePinNote
import com.john_halaka.noteTopia.feature_note.domain.use_case.UpdateNote
import com.john_halaka.noteTopia.feature_note_color.data.data_source.NoteColorDao
import com.john_halaka.noteTopia.feature_note_color.data.repository.NoteColorRepositoryImpl
import com.john_halaka.noteTopia.feature_note_color.domain.repository.NoteColorRepository
import com.john_halaka.noteTopia.feature_note_color.domain.use_case.AddColorUseCase
import com.john_halaka.noteTopia.feature_note_color.domain.use_case.ColorExistsUseCase
import com.john_halaka.noteTopia.feature_note_color.domain.use_case.DeleteAllColorsUseCase
import com.john_halaka.noteTopia.feature_note_color.domain.use_case.DeleteNoteColorUseCase
import com.john_halaka.noteTopia.feature_note_color.domain.use_case.GetNoteColorsUseCase
import com.john_halaka.noteTopia.feature_note_color.domain.use_case.NoteColorUseCases
import com.john_halaka.noteTopia.feature_todo.data.repository.TodoRepositoryImpl
import com.john_halaka.noteTopia.feature_todo.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE note ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0")
        }
    }
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `note_color_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `argb` INTEGER NOT NULL)")
        }
    }
    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE note ADD COLUMN isLocked INTEGER NOT NULL DEFAULT 0")
        }
    }
    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // No changes happen I don't remember having v5 but it says it does!
        }
    }
    private val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS `todo_table` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT,
                `title` TEXT NOT NULL,
                `description` TEXT,
                `completed` INTEGER NOT NULL
            )
        """.trimIndent()
            )
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS `note_color_table` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `argb` INTEGER NOT NULL
            )
        """.trimIndent()
            )
        }
    }

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        )
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5,
                MIGRATION_5_6
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteColorDao(db: NoteDatabase): NoteColorDao {
        return db.noteColorDao
    }

    @Provides
    @Singleton
    fun provideNoteColorRepository(dao: NoteColorDao): NoteColorRepository {
        return NoteColorRepositoryImpl(dao)
    }


    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository, context: Context): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNotes = DeleteNote(repository),
            addNote = AddNote(repository, context),
            getNoteById = GetNoteById(repository),
            getFavouriteNotes = GetFavouriteNotes(repository),
            getLockedNotes = GetLockedNotes(repository),
            updateNote = UpdateNote(repository),
            moveNoteToTrash = MoveNoteToTrash(repository),
            getDeletedNotes = GetDeletedNotes(repository),
            togglePinNote = TogglePinNote(repository),
            toggleLockNote = ToggleLockNote(repository)
        )
    }

    @Provides
    @Singleton
    fun provideNoteColorUseCases(repository: NoteColorRepository): NoteColorUseCases {
        return NoteColorUseCases(
            addColorUseCase = AddColorUseCase(repository),
            deleteAllColorsUseCase = DeleteAllColorsUseCase(repository),
            deleteNoteColorUseCase = DeleteNoteColorUseCase(repository),
            getNoteColorsUseCase = GetNoteColorsUseCase(repository),
            colorExistsUseCase = ColorExistsUseCase(repository)
        )
    }


    @Provides
    @Singleton
    fun provideTodoRepository(db: NoteDatabase): TodoRepository {
        return TodoRepositoryImpl(db.todoDao)
    }


    @Provides
    @Singleton
    fun provideQuoteRepository(): QuoteRepository {
        return QuoteRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }


    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

//    @Provides
//    @Singleton
//    fun provideBiometricPromptManager(@ApplicationContext context: Context): BiometricPromptManager {
//        return BiometricPromptManager(context)
//    }


}