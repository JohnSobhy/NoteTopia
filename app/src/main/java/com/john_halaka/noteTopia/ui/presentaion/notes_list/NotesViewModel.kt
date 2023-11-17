package com.john_halaka.noteTopia.ui.presentaion.notes_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john_halaka.noteTopia.feature_note.data.PreferencesManager
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_note.domain.use_case.NoteUseCases
import com.john_halaka.noteTopia.feature_note.domain.util.NoteOrder
import com.john_halaka.noteTopia.feature_note.domain.util.notesSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null
    private var getNotesJob: Job? = null
    private var getFavoritesJob: Job? = null
    private var getDeletedNotesJob: Job? = null

    private var currentNoteOrder = preferencesManager.getNoteOrder()

    init {
        viewModelScope.launch {
            //delay(300)
            Log.d("NotesViewModel", "calling getFavorites")
            getFavouriteNotes(currentNoteOrder)
        }
        viewModelScope.launch {
            //delay(500)
            Log.d("NotesViewModel", "calling getDeleted")
            getDeletedNotes(currentNoteOrder)
        }
        viewModelScope.launch {
            //delay(1000) // Wait before calling getNotes
            Log.d("NotesViewModel", "calling getNotes")
            getNotes(currentNoteOrder)
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
                    getNotes(event.noteOrder)
                    getDeletedNotes(event.noteOrder)
                }
                updateNoteOrder(event.noteOrder)
                // currentNoteOrder = event.noteOrder

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
                    // delay(500)
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
        getNotesJob?.cancel()
        Log.d("NotesViewModel", "getNotes called")
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
        getFavoritesJob?.cancel()
        Log.d("NotesViewModel", "getFavorites called")
        try {
            getFavoritesJob = noteUseCases.getFavouriteNotes(noteOrder)
                .onEach { notes ->
                    Log.d("NotesViewModel", "getFavNotes: $notes")
                    _state.value = state.value.copy(
                        favouriteNotes = notes,
                        noteOrder = noteOrder
                    )
                }
                .launchIn(viewModelScope)
        } catch (e: Exception) {
            Log.e("NotesViewModel", "Error getting favNotes: ${e.message}")
        }

    }

    private fun getDeletedNotes(noteOrder: NoteOrder) {
        getDeletedNotesJob?.cancel()
        try {
            getDeletedNotesJob = noteUseCases.getDeletedNotes(noteOrder)
                .onEach { notes ->
                    Log.d("NotesViewModel", "getDeletedNotes: $notes")
                    _state.value = state.value.copy(
                        deletedNotes = notes,
                        noteOrder = noteOrder
                    )
                }
                .launchIn(viewModelScope)
        } catch (e: Exception) {
            Log.e("NotesViewModel", "Error getting favNotes: ${e.message}")
        }
    }

    // Save the user's preference whenever currentNoteOrder changes
    private fun updateNoteOrder(noteOrder: NoteOrder) {
        currentNoteOrder = noteOrder
        preferencesManager.saveNoteOrder(noteOrder)

    }

//    fun refreshNotes() {
//        getNotes(currentNoteOrder)
//    }


}
