package com.john_halaka.noteTopia.ui.presentaion.add_edit_note.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.ui.presentaion.add_edit_note.AddEditNoteEvent
import com.john_halaka.noteTopia.ui.presentaion.add_edit_note.AddEditNoteViewModel
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.mToast
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    context: Context,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val snackbarHostState = remember { SnackbarHostState() }
    val currentNote = viewModel.note.value
    val noteId = currentNote.id
    var isFavorite: Boolean = currentNote.isFavourite

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }

                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is AddEditNoteViewModel.UiEvent.DeleteNote -> {
                    navController.navigateUp()

                }

                is AddEditNoteViewModel.UiEvent.NavigateBack -> {
                    navController.navigateUp()
                }
            }

        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                        Text(text = currentNote.title)
                        },

                navigationIcon = {
                    IconButton(onClick = {
                        Log.d("addEditScreen", "backButton is clicked noteId = $noteId")
                        viewModel.onEvent(AddEditNoteEvent.BackButtonClick)
                        if (noteId != null) {
                            mToast(context, context.resources.getString(R.string.note_saved))
                        }
                    })
                    {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }

                },
                actions = {

                    IconButton(
                        onClick = {
                            if (
                                noteId == null ||
                                titleState.text.isBlank() ||
                                contentState.text.isBlank()
                            ) {
                                Log.d("addEditScreen", "currentNote is Null noteId= $noteId")
                                navController.navigateUp()

                            } else {
                                Log.d("addEditScreen", "currentNote is Not Null noteId= $noteId")
                                viewModel.onEvent(
                                    AddEditNoteEvent.MoveNoteToTrash(
                                        currentNote.copy(
                                            isDeleted = !currentNote.isDeleted,
                                            isFavourite = false
                                        )
                                    )
                                )

                                mToast(context, context.resources.getString(R.string.note_moved_to_trash))
                            }
                        }
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.delete_note),

                            )

                    }
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            viewModel.onEvent(AddEditNoteEvent.ChangeIsFavorite(isFavorite))
                        }
                    ) {
                        Icon(
                            imageVector = if (viewModel.noteIsFavorite.value)
                                ImageVector.vectorResource(R.drawable.fav_note_selected)
                            else ImageVector.vectorResource(R.drawable.fav_note_unselected),
                            tint = Color.Unspecified,
                            contentDescription = stringResource(R.string.mark_note_as_favorite)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    Log.d("addEditScreen", "saveButton is clicked noteId = $noteId")
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                    if (titleState.text.isNotBlank() && contentState.text.isNotBlank())
                        mToast(context, context.resources.getString(R.string.note_saved))
                },
//                containerColor = MaterialTheme.colorScheme.primaryContainer,
//                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,

            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_save_24),
                    contentDescription = stringResource(R.string.save),

                    )

            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items(Note.noteColors) { color ->
                    val colorInt = color.toArgb()

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.noteColor.value == colorInt) {
                                    MaterialTheme.colorScheme.onSurface
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )
                    Spacer(Modifier.width(4.dp))
                }
            }


            Spacer(Modifier.height(16.dp))

            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle =
                MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimary),

                )

            Spacer(Modifier.height(16.dp))

            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))

                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimary),

                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}
