package com.john_halaka.noteTopia.ui.presentaion.notes_list

import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_note.domain.util.NoteOrder
import com.john_halaka.noteTopia.feature_note.domain.util.OrderType

data class NotesState(
    val favouriteNotes: List<Note> = emptyList(),
    val notes: List<Note> = emptyList(),
    val deletedNotes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val searchResult: List<Note> = emptyList(),

    )
