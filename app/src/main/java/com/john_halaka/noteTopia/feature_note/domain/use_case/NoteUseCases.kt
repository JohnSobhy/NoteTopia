package com.john_halaka.noteTopia.feature_note.domain.use_case

data class NoteUseCases(

    val getNotes: GetNotes,
    val deleteNotes: DeleteNote,
    val addNote: AddNote,
    val getNoteById: GetNoteById,
    val getFavouriteNotes: GetFavouriteNotes,
    val getDeletedNotes: GetDeletedNotes,
    val updateNote: UpdateNote,
    val moveNoteToTrash: MoveNoteToTrash,
    val togglePinNote: TogglePinNote
)
