package com.john_halaka.noteTopia.ui.presentaion.add_edit_note

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_note.domain.model.InvalidNoteException
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_note.domain.use_case.NoteUseCases
import com.john_halaka.noteTopia.feature_note_color.domain.model.NoteColor
import com.john_halaka.noteTopia.feature_note_color.domain.use_case.NoteColorUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val noteColorUsesCases: NoteColorUseCases,
    savedStateHandle: SavedStateHandle,
    context: Context
) : ViewModel() {
    private val _noteTitle = mutableStateOf(
        NoteTextFieldState(
             hint = context.resources.getString(R.string.enter_title)
        )
    )
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(
        NoteTextFieldState(
            hint = context.resources.getString(R.string.enter_the_content_of_your_note)
        )
    )
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableIntStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _tempNoteColor = mutableIntStateOf(_noteColor.intValue)
    val tempNoteColor: State<Int> = _tempNoteColor

    private val _noteIsFavorite = mutableStateOf(false)
    val noteIsFavorite: State<Boolean> = _noteIsFavorite

    private val _noteIsLocked = mutableStateOf(false)
    val noteIsLocked: State<Boolean> = _noteIsLocked

    private val _noteIsPinned = mutableStateOf(false)
    val noteIsPinned: State<Boolean> = _noteIsPinned

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null
    val noteId: Int = currentNoteId ?: -1

    private val _savedNoteColors = MutableLiveData<List<NoteColor>>()
    val savedNoteColors = _savedNoteColors

    private val _note = mutableStateOf(
        Note(
            title = noteTitle.value.text,
            content = noteContent.value.text,
            color = noteColor.value,
            timestamp = System.currentTimeMillis()
        )
    )
    var note: State<Note> = _note
    private val snackBarMessage = context.resources.getString(R.string.couldn_t_save_note)

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                Log.d("AddEditNoteViewModel", "getNoteById is call $noteId")
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
                        _tempNoteColor.intValue = note.color
                        _noteColor.intValue = note.color
                        Log.d("color", "note color is $_noteColor")
                        _noteIsFavorite.value = note.isFavourite
                        _noteIsPinned.value = note.isPinned
                        _noteIsLocked.value = note.isLocked
                    }
                }
            }
            Log.d("AddEditNoteViewModel", "getNoteById is Not call $noteId")
        }
        // Fetch the saved colors
        fetchNoteColors()
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
                _tempNoteColor.intValue = event.color
                Log.d("changeTempColor()", "${_tempNoteColor.intValue}")
            }

            is AddEditNoteEvent.ChangeIsFavorite -> {
                _noteIsFavorite.value = event.isFavorite
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        Log.d("AddEditNoteViewModel", "saveButton call id=$noteId")
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId,
                                isFavourite = noteIsFavorite.value,
                                isPinned = noteIsPinned.value,
                                isLocked = noteIsLocked.value
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: snackBarMessage
                            )
                        )

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
                        noteTitle.value.text.isBlank() ||
                        noteContent.value.text.isBlank()
                    ) {

                        _eventFlow.emit(UiEvent.NavigateBack)

                    } else {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = snackBarMessage
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
                    event.note.id?.let { noteId ->
                        noteUseCases.moveNoteToTrash(noteId, event.note.isDeleted)
                        noteUseCases.updateNote(noteId, event.note.isFavourite)
                        noteUseCases.toggleLockNote(noteId, event.note.isLocked)
                    }

                    _eventFlow.emit(UiEvent.DeleteNote)
                }
            }
        }

    }

    fun doneClicked() {
        viewModelScope.launch {
            _noteColor.intValue = _tempNoteColor.intValue
        }
    }

    fun cancelClicked() {
        viewModelScope.launch {
            _tempNoteColor.intValue = _noteColor.intValue
        }
    }

    // Function to fetch all the saved colors
    fun fetchNoteColors() {
        viewModelScope.launch {
            _savedNoteColors.value = noteColorUsesCases.getNoteColorsUseCase()
        }
    }

    // Function to add a color
    fun addNoteColor(argb: Int) {
        viewModelScope.launch {
            // Check if the color already exists in the database
            val colorCount = noteColorUsesCases.colorExistsUseCase(argb)
            if (colorCount == 0) {
                // If the color doesn't exist, insert it
                noteColorUsesCases.addColorUseCase(argb)
                fetchNoteColors() // Refresh the colors
            }
        }
    }


    // Function to delete all colors
    fun deleteAllNoteColors() {
        viewModelScope.launch {
            noteColorUsesCases.deleteAllColorsUseCase()
            fetchNoteColors() // Refresh the colors
        }
    }

    // Function to delete a specific color
    fun deleteNoteColor(noteColor: NoteColor) {
        viewModelScope.launch {
            noteColorUsesCases.deleteNoteColorUseCase(noteColor)
            fetchNoteColors() // Refresh the colors
        }
    }


    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()

        object DeleteNote : UiEvent()

        object NavigateBack : UiEvent()
    }
}

