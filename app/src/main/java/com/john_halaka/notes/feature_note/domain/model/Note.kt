package com.john_halaka.notes.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.john_halaka.notes.ui.theme.BabyBlue
import com.john_halaka.notes.ui.theme.LightGreen
import com.john_halaka.notes.ui.theme.RedOrange
import com.john_halaka.notes.ui.theme.RedPink
import com.john_halaka.notes.ui.theme.Violet

@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp : Long,
    val color : Int
){
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}
