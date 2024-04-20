package com.john_halaka.noteTopia.feature_note.domain.use_case

import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_note.domain.repository.NoteRepository
import com.john_halaka.noteTopia.feature_note.domain.use_case.GetNotes.Companion.sortNotes
import com.john_halaka.noteTopia.feature_note.domain.util.NoteOrder
import com.john_halaka.noteTopia.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDeletedNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getDeletedNotes().map { notes ->
            val deletedNotes = sortNotes(notes, noteOrder)
            deletedNotes
        }
    }
}