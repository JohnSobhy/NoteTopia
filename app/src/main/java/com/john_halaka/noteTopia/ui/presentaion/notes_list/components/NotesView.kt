package com.john_halaka.noteTopia.ui.presentaion.notes_list.components

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
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.ui.Screen
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesEvent
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesViewModel
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
    val cellHeightDpValue = 140.dp



    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp)

    ) {
        items(notesList) { note ->
            val dropDownItems = mutableListOf<DropDownItem>()
            if (note.isPinned) {
                dropDownItems.add(
                    DropDownItem(
                        stringResource(R.string.unpin),
                        icon = Icons.Outlined.PushPin
                    )
                )
            } else {
                dropDownItems.add(
                    DropDownItem(
                        stringResource(R.string.pin),
                        icon = Icons.Outlined.PushPin
                    )
                )
            }
            dropDownItems.add(
                DropDownItem(
                    stringResource(R.string.delete),
                    icon = Icons.Outlined.Delete
                )
            )

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
                        mToast(context, context.resources.getString(R.string.removed_from_favourites))
                    else
                        mToast(context, context.resources.getString(R.string.added_to_favorites))

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
                    when (item.text) {

                        context.resources.getString(R.string.delete) -> {
                            viewModel.onEvent(
                                NotesEvent.MoveNoteToTrash(
                                    note.copy(
                                        isDeleted = !note.isDeleted
                                    )
                                )
                            )
                            mToast(
                                context,
                                context.resources.getString(R.string.note_moved_to_trash)
                            )
                        }

                        context.resources.getString(R.string.pin) -> {
                            viewModel.onEvent(NotesEvent.PinNote(note))
                            mToast(context, context.resources.getString(R.string.note_pinned))
                        }

                        context.resources.getString(R.string.unpin) -> {
                            viewModel.onEvent(NotesEvent.UnpinNote(note))
                            mToast(context, context.resources.getString(R.string.note_unpinned))
                        }
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        items(notesList) { note ->
            val dropDownItems = mutableListOf<DropDownItem>()
            if (note.isPinned) {
                dropDownItems.add(
                    DropDownItem(
                        stringResource(R.string.unpin),
                        icon = Icons.Outlined.PushPin
                    )
                )
            } else {
                dropDownItems.add(
                    DropDownItem(
                        stringResource(R.string.pin),
                        icon = Icons.Outlined.PushPin
                    )
                )
            }
            dropDownItems.add(
                DropDownItem(
                    stringResource(R.string.delete),
                    icon = Icons.Outlined.Delete
                )
            )

            NoteItem(
                note = note,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                onFavoriteClick = {
                    if (note.isFavourite)
                        mToast(
                            context,
                            context.resources.getString(R.string.removed_from_favourites)
                        )
                    else
                        mToast(context, context.resources.getString(R.string.added_to_favorites))

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
                    when (item.text) {

                        context.resources.getString(R.string.delete) -> {
                            viewModel.onEvent(
                                NotesEvent.MoveNoteToTrash(
                                    note.copy(
                                        isDeleted = !note.isDeleted
                                    )
                                )
                            )
                            mToast(
                                context,
                                context.resources.getString(R.string.note_moved_to_trash)
                            )
                        }

                        context.resources.getString(R.string.pin) -> {
                            viewModel.onEvent(NotesEvent.PinNote(note))
                            mToast(context, context.resources.getString(R.string.note_pinned))
                        }

                        context.resources.getString(R.string.unpin) -> {
                            viewModel.onEvent(NotesEvent.UnpinNote(note))
                            mToast(context, context.resources.getString(R.string.note_unpinned))
                        }
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
