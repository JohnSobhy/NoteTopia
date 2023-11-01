package com.john_halaka.notes.ui.presentaion.notes_list.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.ui.Screen
import com.john_halaka.notes.ui.presentaion.notes_list.NotesEvent
import com.john_halaka.notes.ui.presentaion.notes_list.NotesViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun GridViewNotes(

    navController: NavController,
    viewModel: NotesViewModel,
    notesList: List<Note>,
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    showFavoriteIcon: Boolean,
) {
    // to convert px to dp in different screens 

//    val heightPxValue = 450
//    val density = LocalDensity.current.density
    val cellHeightDpValue = 120.dp
    val dropDownItems = listOf(
        DropDownItem("Delete", icon = Icons.Outlined.Delete)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp)

    ) {
        items(notesList) { note ->

            NoteItem(
                note = note,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cellHeightDpValue)
                    .clickable {
                        navController.navigate(
                            Screen.AddEditNoteScreen.route +
                                    "?noteId=${note.id}&noteColor=${note.color}"
                        )
                    },
                onFavoriteClick = {
                    if (note.isFavourite)
                        mToast(context, "Removed from Favourites")
                    else
                        mToast(context, "Added to Favorites")

                    viewModel.onEvent(
                        NotesEvent.UpdateNote(
                            note.copy(
                                isFavourite = !note.isFavourite
                            )
                        )
                    )
                },
                showFavoriteIcon = showFavoriteIcon,
                //showCheckBox = showCheckBox,
                dropDownItems = dropDownItems,
                onItemClick = { item ->
                    if (item.text == "Delete") {
                        viewModel.onEvent(
                            NotesEvent.MoveNoteToTrash(
                                note.copy(
                                    isDeleted = !note.isDeleted
                                )
                            )
                        )
                        mToast(context, "Note Moved to trash")
                    } else {

                    }
                },
                onClick = {
                    navController.navigate(
                        Screen.AddEditNoteScreen.route +
                                "?noteId=${note.id}&noteColor=${note.color}"
                    )
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
    notesList: List<Note>,
    context: Context,
    showFavoriteIcon: Boolean,
    //showCheckBox: Boolean
) {
    val dropDownItems = listOf(
        DropDownItem("Delete", icon = Icons.Outlined.Delete)
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        items(notesList) { note ->
            NoteItem(
                note = note,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                onFavoriteClick = {
                    if (note.isFavourite)
                        mToast(context, "Removed from Favourites")
                    else
                        mToast(context, "Added to Favorites")

                    viewModel.onEvent(
                        NotesEvent.UpdateNote(
                            note.copy(
                                isFavourite = !note.isFavourite
                            )
                        )
                    )

                },
                showFavoriteIcon = showFavoriteIcon,
                // showCheckBox = showCheckBox
                dropDownItems = dropDownItems,
                onItemClick = { item ->
                    if (item.text == "Delete") {
                        viewModel.onEvent(
                            NotesEvent.MoveNoteToTrash(
                                note.copy(
                                    isDeleted = !note.isDeleted
                                )
                            )
                        )
                        mToast(context, "Note Moved to trash")
                    } else {

                    }
                },
                onClick = {
                    navController.navigate(
                        Screen.AddEditNoteScreen.route +
                                "?noteId=${note.id}&noteColor=${note.color}"
                    )
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

        }

    }
}
