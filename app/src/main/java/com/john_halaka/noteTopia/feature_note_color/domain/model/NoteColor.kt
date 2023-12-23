package com.john_halaka.noteTopia.feature_note_color.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_color_table")
data class NoteColor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val argb: Int
)
