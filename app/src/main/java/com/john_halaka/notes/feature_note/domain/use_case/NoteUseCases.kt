package com.john_halaka.notes.feature_note.domain.use_case

data class NoteUseCases(

    val getNotes: GetNotes,
    val deleteNotes: DeleteNote,
    val addNote: AddNote,
    val getNoteById: GetNoteById
)
