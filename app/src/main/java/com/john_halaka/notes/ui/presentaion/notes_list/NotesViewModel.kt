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

    init {
        viewModelScope.launch {
            delay(500) // Wait before calling getNotes
            Log.d("NotesViewModel", "Before calling getNotes")
            getNotes(initialNoteOrder)
        }
        Log.d("NotesViewModel", "After calling getNotes")
        getFavouriteNotes(initialNoteOrder)
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
                getNotes(event.noteOrder)

            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNotes(event.note)
                    recentlyDeletedNote = event.note
                }
            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
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
                viewModelScope.launch {
                    noteUseCases.updateNote(event.note.id!!, event.note.isFavourite)
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
}
