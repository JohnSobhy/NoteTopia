package com.john_halaka.noteTopia.feature_note.domain.util

import com.john_halaka.noteTopia.feature_note.domain.model.Note

fun notesSearch(notes: List<Note>, searchPhrase: String): List<Note> {
    return notes.filter { note ->
        note.title.contains(searchPhrase, ignoreCase = true) ||
                note.content.contains(searchPhrase, ignoreCase = true)
    }
}