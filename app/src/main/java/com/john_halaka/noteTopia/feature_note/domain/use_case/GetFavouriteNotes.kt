package com.john_halaka.noteTopia.feature_note.domain.use_case

import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_note.domain.repository.NoteRepository
import com.john_halaka.noteTopia.feature_note.domain.use_case.GetNotes.Companion.sortNotes
import com.john_halaka.noteTopia.feature_note.domain.util.NoteOrder
import com.john_halaka.noteTopia.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavouriteNotes(
    private val repository: NoteRepository
) {
    // every useCase needs to have only one public function that executes that useCase
    // it can have private functions

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getFavouriteNotes().map { notes ->
            val (pinnedNotes, unpinnedNotes) = notes.partition { it.isPinned }
            val sortedPinnedNotes = sortNotes(pinnedNotes, noteOrder)
            val sortedUnpinnedNotes = sortNotes(unpinnedNotes, noteOrder)
            sortedPinnedNotes + sortedUnpinnedNotes
        }
    }

}
