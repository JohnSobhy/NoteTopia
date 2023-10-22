package com.john_halaka.notes.ui.presentaion.deleted_notes

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.notes.ui.presentaion.notes_list.NotesEvent
import com.john_halaka.notes.ui.presentaion.notes_list.NotesViewModel
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletedNotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
    context: Context
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val notesList = state.deletedNotes
    val dropDownItems = listOf(
        DropDownItem("Restore"),
        DropDownItem("Delete Permanently")
    )
    val cellHeight = 120.dp

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Recently deleted notes") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    })
                    {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back")
                    }

                },
                actions = {
                    IconButton(
                        onClick = {

                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Empty trash can"
                        )

                    }
                }
            )

        },

        ) { values ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(values)

        ) {
            items(notesList) { note ->

                DeletedNoteItem(
                    note = note,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cellHeight),
                    dropDownItems = dropDownItems,
                    onItemClick = { item ->
                        if (item == DropDownItem("Restore")) {
                            viewModel.onEvent(
                                NotesEvent.MoveNoteToTrash(
                                    note.copy(
                                        isDeleted = !note.isDeleted
                                    )
                                )
                            )
                            mToast(context, "Note Restored")
                        } else {
                            viewModel.onEvent(NotesEvent.DeleteNote(note))
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Note removed",
                                    actionLabel = "Undo"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(NotesEvent.RestoreNote)
                                }
                            }
                        }

                    }
                )
                }

            }
        }
    }


private fun mToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}