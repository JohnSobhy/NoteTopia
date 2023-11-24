package com.john_halaka.noteTopia.feature_note.domain.use_case

import android.content.Context
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_note.domain.model.InvalidNoteException
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_note.domain.repository.NoteRepository

class AddNote(
    private val repository: NoteRepository,
    private val context: Context
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException(context.resources.getString(R.string.the_title_cannot_be_empty))
        }
        if (note.content.isBlank()) {
            throw InvalidNoteException(context.resources.getString(R.string.the_content_of_the_note_cannot_be_empty))
        }
        repository.insertNote(note)
    }
}