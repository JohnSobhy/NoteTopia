package com.john_halaka.notes.ui.presentaion.add_edit_note

import androidx.compose.ui.focus.FocusState
import com.john_halaka.notes.feature_note.domain.model.Note

sealed class AddEditNoteEvent{

    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class EnteredContent(val value: String) : AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class ChangeColor(val color: Int) : AddEditNoteEvent()

    data class ChangeIsFavorite(val isFavorite: Boolean) : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()

    //   data class DeleteNote(val noteId: Int) : AddEditNoteEvent()
    //  data class UpdateNote(val note: Note) : AddEditNoteEvent()
    data class MoveNoteToTrash(val note: Note) : AddEditNoteEvent()

    object BackButtonClick : AddEditNoteEvent()
}


