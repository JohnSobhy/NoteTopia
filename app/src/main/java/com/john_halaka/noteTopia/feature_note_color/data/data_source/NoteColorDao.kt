package com.john_halaka.noteTopia.feature_note_color.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.john_halaka.noteTopia.feature_note_color.domain.model.NoteColor

@Dao
interface NoteColorDao {
    @Query("SELECT * FROM note_color_table")
    suspend fun getAll(): List<NoteColor>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(color: NoteColor)

    @Delete
    suspend fun delete(color: NoteColor)

    @Query("DELETE FROM note_color_table")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM note_color_table WHERE argb = :argb")
    suspend fun colorExists(argb: Int): Int

}