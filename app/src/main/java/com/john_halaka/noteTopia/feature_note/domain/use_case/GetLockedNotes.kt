package com.john_halaka.noteTopia.feature_note.domain.use_case

import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_note.domain.repository.NoteRepository
import com.john_halaka.noteTopia.feature_note.domain.util.NoteOrder
import com.john_halaka.noteTopia.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLockedNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getLockedNotes().map { notes ->
            val (pinnedNotes, unpinnedNotes) = notes.partition { it.isPinned }
            val sortedPinnedNotes = GetNotes.sortNotes(pinnedNotes, noteOrder)
            val sortedUnpinnedNotes = GetNotes.sortNotes(unpinnedNotes, noteOrder)
            sortedPinnedNotes + sortedUnpinnedNotes
        }
    }

}
