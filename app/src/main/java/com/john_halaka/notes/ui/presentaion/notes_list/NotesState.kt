package com.john_halaka.notes.ui.presentaion.notes_list

import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.feature_note.domain.util.NoteOrder
import com.john_halaka.notes.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.MutableStateFlow

data class NotesState(
    val notes: List<Note> = emptyList(),
    val favouriteNotes: List<Note> = emptyList(),
    val deletedNotes: MutableStateFlow<List<Note>> = MutableStateFlow(emptyList()),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val searchResult: List<Note> = emptyList(),

    )
