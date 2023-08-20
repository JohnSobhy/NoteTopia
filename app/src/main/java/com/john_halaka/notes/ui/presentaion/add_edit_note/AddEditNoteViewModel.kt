package com.john_halaka.notes.ui.presentaion.add_edit_note

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john_halaka.notes.feature_note.domain.model.InvalidNoteException
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases ,
    savedStateHandle: SavedStateHandle
) : ViewModel () {
    private val _noteTitle = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter Title.."
        )
    )
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter the content of your note"
        )
    )
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null
    val noteId: Int = currentNoteId ?: -1
    private var recentlyDeletedNote: Note? = null

    private val _note = mutableStateOf(
        Note(
            title = noteTitle.value.text,
            content = noteContent.value.text,
            color = noteColor.value,
            timestamp = System.currentTimeMillis()
        )
    )
    val note: State<Note> = _note

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNoteById(noteId)?.also { note ->
                        _note.value = note
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color

                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        Log.d("AddEditNoteViewModel", "onEvent: $event")
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )

                    }
                }
            }

            is AddEditNoteEvent.DeleteNote -> {
                viewModelScope.launch {

                    currentNoteId?.let { noteUseCases.getNoteById(it) }
                        ?.let { note ->
                            noteUseCases.deleteNotes(note)
                            recentlyDeletedNote = note

                            _eventFlow.emit(UiEvent.DeleteNote)
                        }

                }
            }

            is AddEditNoteEvent.BackButtonClick -> {
                viewModelScope.launch {
                    if (
                        noteTitle.value.text.isNotBlank() &&
                        noteContent.value.text.isNotBlank()
                    ) {
                        onEvent(AddEditNoteEvent.SaveNote)

                    } else if (
                        noteTitle.value.text.isBlank() &&
                        noteContent.value.text.isBlank()
                    ) {

                        _eventFlow.emit(UiEvent.NavigateBack)

                    } else {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = "Couldn't save note"
                            )
                        )
                    }


                }
            }

            is AddEditNoteEvent.MoveNoteToTrash -> {
                Log.d("AddEditNoteViewModel", "trashButtonClicked")
                viewModelScope.launch {
                    if (
                        noteTitle.value.text.isBlank() ||
                        noteContent.value.text.isBlank()
                    ) {
                        _eventFlow.emit(UiEvent.NavigateBack)
                    } else
                        Log.d("AddEditNoteViewModel", "MoveToTrash called")
                    noteUseCases.moveNoteToTrash(event.note.id!!, event.note.isDeleted)
                    _eventFlow.emit(UiEvent.DeleteNote)
                }
            }
        }

    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()

        object DeleteNote : UiEvent()

        object NavigateBack : UiEvent()
    }
}

