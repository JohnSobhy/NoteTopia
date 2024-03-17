package com.john_halaka.noteTopia.ui.presentaion.deleted_notes

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesEvent
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesViewModel
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.NoteGrid
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.SortDropDownMenu
import com.john_halaka.noteTopia.ui.theme.BabyBlue
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletedNotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
    context: Context
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val notesList = state.deletedNotes

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.recently_deleted_notes)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    })
                    {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }

                },
                actions = {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(NotesEvent.ToggleOrderSection)
                                expanded = !expanded
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = stringResource(R.string.sort_notes)
                            )
                        }
                        SortDropDownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            noteOrder = state.noteOrder,
                            onOrderChange = {
                                viewModel.onEvent(NotesEvent.Order(it))
                            }
                        )

                    }
                }
            )

        },

        ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)

        ) {

            if (state.deletedNotes.isEmpty()) {
                var showProgress by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(1000)
                    showProgress = false
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (showProgress) {
                        CircularProgressIndicator(color = BabyBlue)
                    } else
                        Text(text = stringResource(R.string.trash_can_is_empty))
                }
            } else {
                NoteGrid(
                    navController = navController,
                    viewModel = viewModel,
                    notesList = notesList,
                    context = context,
                    showFavoriteIcon = false,
                    isClickable = false,
                    showDefaultDropDownMenu = false,
                    columns = 2,
                    cellHeightDpValue = 120.dp
                )
            }
        }
    }
}
