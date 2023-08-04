package com.john_halaka.notes.ui.presentaion.notes_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.ui.presentaion.notes_list.NotesEvent
import com.john_halaka.notes.ui.presentaion.notes_list.NotesViewModel
import com.john_halaka.notes.ui.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun GridViewNotes (

    navController: NavController,
    viewModel: NotesViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    notesList: List<Note>
){
    val cellHeight : Dp = 150.dp
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),

        ){
        items(notesList) {note ->

            NoteItem(
                note = note,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cellHeight)
                    .clickable {
                        navController.navigate(
                            Screen.AddEditNoteScreen.route +
                                    "?noteId=${note.id}&noteColor=${note.color}"
                        )
                    },
                onDeleteClick = {
                    viewModel.onEvent(NotesEvent.DeleteNote(note))

                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Note deleted",
                            actionLabel = "Undo"
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(NotesEvent.RestoreNote)
                        }
                    }
                }
            )
        }



    }
}
@Composable
fun ListViewNotes(
    navController: NavController,
    viewModel: NotesViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    notesList: List<Note>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(notesList) { note ->
            NoteItem(
                note = note,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(
                            Screen.AddEditNoteScreen.route +
                                    "?noteId=${note.id}&noteColor=${note.color}"
                        )
                    },
                onDeleteClick = {
                    viewModel.onEvent(NotesEvent.DeleteNote(note))

                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Note deleted",
                            actionLabel = "Undo"
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(NotesEvent.RestoreNote)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

        }

    }
}