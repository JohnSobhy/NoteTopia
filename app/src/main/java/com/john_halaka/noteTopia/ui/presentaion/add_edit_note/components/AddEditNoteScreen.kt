package com.john_halaka.noteTopia.ui.presentaion.add_edit_note.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.noteTopia.R
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

    val showColorMenu = remember { mutableStateOf(false) }

    val anchor = remember { mutableStateOf(Offset.Zero) }

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
                    Text(
                        text = currentNote.title,
                        maxLines = 1
                    )
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
                    Box {
                        IconButton(
                            onClick = {
                                showColorMenu.value = true
                            },

                            ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.note_color_trans),
                                contentDescription = stringResource(R.string.note_color),
                                tint = Color(viewModel.tempNoteColor.value),
                                modifier = Modifier.onGloballyPositioned { coordinates ->
                                    anchor.value = coordinates.positionInRoot()
                                }
                            )
                        }
                        DropdownMenu(
//                            offset = DpOffset(
//                                x = (anchor.value.x.dp), // need a way to make the menu goes from end to start when near the edge
//                                y = (anchor.value.y.dp - 50.dp) // the -50 value is based on trial and error only
//                            ),
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.onSecondary),
                            expanded = showColorMenu.value,
                            onDismissRequest = {
                                showColorMenu.value = false
                                viewModel.cancelClicked()
                            }
                        ) {
                            DropdownMenuItem(
                                enabled = false,
                                modifier = Modifier.fillMaxSize(),
                                text = {
                                    ColorPicker(
                                        onColorChange = { colorEnvelope ->
                                            viewModel.onEvent(
                                                AddEditNoteEvent.ChangeColor(
                                                    colorEnvelope.color.toArgb()
                                                )
                                            )
                                            Log.d(
                                                "colorEnvelope",
                                                "the hex code is ${colorEnvelope.hexCode}"
                                            )
                                        },
                                        viewModel = viewModel,

                                        onCancelClick = {
                                            showColorMenu.value = false
                                        },
                                        onDoneClick = {
                                            showColorMenu.value = false
                                        },
                                    )
                                },
                                onClick = { }
                            )
                        }
                    }

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

                                mToast(
                                    context,
                                    context.resources.getString(R.string.note_moved_to_trash)
                                )
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
        bottomBar = {
            Spacer(modifier = Modifier.navigationBarsPadding())
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

            Spacer(Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {

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
}
