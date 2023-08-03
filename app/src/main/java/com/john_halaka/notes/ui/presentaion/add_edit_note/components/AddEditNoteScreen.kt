package com.john_halaka.notes.ui.presentaion.add_edit_note.components

import android.annotation.SuppressLint
import android.content.pm.ChangedPackages
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.notes.R
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.ui.presentaion.add_edit_note.AddEditNoteEvent
import com.john_halaka.notes.ui.presentaion.add_edit_note.AddEditNoteViewModel
import com.john_halaka.notes.ui.presentaion.notes.NotesEvent
import com.john_halaka.notes.ui.presentaion.notes.NotesViewModel
import com.john_halaka.notes.ui.Screen
import com.john_halaka.notes.ui.theme.Typography
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {

    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val noteId = viewModel.noteId
    val snackbarHostState = remember { SnackbarHostState ()}

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(
                if (noteColor != -1) noteColor
                 else viewModel.noteColor.value
            )
        )
    }
    val scope = rememberCoroutineScope()

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
                is AddEditNoteViewModel.UiEvent.DeleteNote ->{
                    navController.navigateUp()

                }

               is AddEditNoteViewModel.UiEvent.NavigateBack -> {
                    navController.navigateUp()
                }
            }

        }
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
            },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,

            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_save_24),
                    contentDescription = "Save",

                )

            }
        } ,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer ,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),


                title = {

                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(AddEditNoteEvent.BackButtonClick)
                        viewModel.onEvent(AddEditNoteEvent.SaveNote)

                    })
                    {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back")
                    }

                },
                actions = {

                    IconButton(
                        onClick = {
                            viewModel.onEvent(AddEditNoteEvent.DeleteNote(noteId))
                        }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete note"
                        )

                    }
                }
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items (Note.noteColors){ color ->
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
                                   MaterialTheme.colorScheme.secondary
                               } else Color.Transparent,
                               shape = CircleShape
                           )
                           .clickable {
//                               scope.launch {
//                                   noteBackgroundAnimatable.animateTo(
//                                       targetValue = Color(colorInt),
//                                       animationSpec = tween(
//                                           durationMillis = 500
//                                       )
//                                   )
//                               }
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
                             viewModel.onEvent(AddEditNoteEvent.EnteredTitle (it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus (it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true ,
                textStyle =
                    MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimary) ,

            )

            Spacer(Modifier.height(16.dp))

            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent (it))

                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus (it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimary)  ,

                 modifier = Modifier.fillMaxHeight()
            )
        }
    }
}