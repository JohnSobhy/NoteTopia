package com.john_halaka.notes.ui.presentaion.notes_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.feature_note.domain.use_case.NoteUseCases
import com.john_halaka.notes.feature_note.domain.util.NoteOrder
import com.john_halaka.notes.feature_note.domain.util.OrderType
import com.john_halaka.notes.feature_note.domain.util.notesSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null
    private val initialNoteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    private var currentNoteOrder = initialNoteOrder
    init {
        viewModelScope.launch {
            Log.d("NotesViewModel", "After calling getNotes")
            getFavouriteNotes(initialNoteOrder)
        }
        viewModelScope.launch {
            delay(500)
            Log.d("NotesViewModel", "After calling getFavorites and before getDeleted")
            getDeletedNotes(initialNoteOrder)
        }
        viewModelScope.launch {
            delay(1000) // Wait before calling getNotes
            Log.d("NotesViewModel", "Before calling getNotes")
            getNotes(initialNoteOrder)
        }

    }

    fun onEvent(event: NotesEvent) {
        Log.d("NotesViewModel", "onEvent: $event")
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                viewModelScope.launch {
                    getFavouriteNotes(event.noteOrder)
                    delay(500)
                    getNotes(event.noteOrder)
                }
                currentNoteOrder = event.noteOrder

            }

            is NotesEvent.DeleteNote -> {
                // used when removing a note permanently from the trash
                viewModelScope.launch {
                    noteUseCases.deleteNotes(event.note)
                    recentlyDeletedNote = event.note
                    getDeletedNotes(currentNoteOrder)
                }
            }

            is NotesEvent.RestoreNote -> {
                // used when the undo button is clicked after removing a note permanently from the trash
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                    getDeletedNotes(currentNoteOrder)
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is NotesEvent.SearchNotes -> {
                _state.value = state.value.copy(
                    searchResult = notesSearch(state.value.notes, event.searchPhrase)
                )

            }

            is NotesEvent.UpdateNote -> {
                Log.d("HomeScreen", "OnFavourite clicked isFavorite= ${event.note.isFavourite}")
                viewModelScope.launch {
                    noteUseCases.updateNote(event.note.id!!, event.note.isFavourite)
                    getFavouriteNotes(currentNoteOrder)
                    delay(500)
                    getNotes(currentNoteOrder)
                }
            }


            is NotesEvent.MoveNoteToTrash -> {
                //used when restoring a note back from the trash
                viewModelScope.launch {
                    noteUseCases.moveNoteToTrash(event.note.id!!, event.note.isDeleted)
                    getDeletedNotes(currentNoteOrder)
                }
            }
        }
    }


    private fun getNotes(noteOrder: NoteOrder) {
        Log.d("NotesViewModel", "getNotes called")
        getNotesJob?.cancel()
        try {
            getNotesJob = noteUseCases.getNotes(noteOrder)
                .onEach { notes ->
                    Log.d("NotesViewModel", "getNotes: $notes")
                    _state.value = state.value.copy(
                        notes = notes,
                        noteOrder = noteOrder
                    )
                }
                .launchIn(viewModelScope)
        } catch (e: Exception) {
            Log.e("NotesViewModel", "Error getting notes: ${e.message}")
        }
    }

    private fun getFavouriteNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getFavouriteNotes(noteOrder)
            .onEach { notes ->
                Log.d("NotesViewModel", "getFavNotes: $notes")
                _state.value = state.value.copy(
                    favouriteNotes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

    private fun getDeletedNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getDeletedNotes(noteOrder)
            .onEach { notes ->
                Log.d("NotesViewModel", "getDeletedNotes: $notes")
                _state.value = state.value.copy(
                    deletedNotes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}
