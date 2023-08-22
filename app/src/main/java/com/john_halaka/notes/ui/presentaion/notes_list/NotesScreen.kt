package com.john_halaka.notes.ui.presentaion.notes_list

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.notes.BottomNavigationBar
import com.john_halaka.notes.R
import com.john_halaka.notes.feature_note.domain.util.ViewType
import com.john_halaka.notes.ui.Screen
import com.john_halaka.notes.ui.presentaion.notes_list.components.GridViewNotes
import com.john_halaka.notes.ui.presentaion.notes_list.components.ListViewNotes
import com.john_halaka.notes.ui.presentaion.notes_list.components.OrderSection
import com.john_halaka.notes.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
    context: Context
    ) {
    val state = viewModel.state.value
    Log.d("NotesScreen", "state: $state")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentViewType by remember { mutableStateOf(ViewType.GRID) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer ,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer

                ),
                title = {
                    Text(text = "Notes", style = Typography.headlineLarge)
                },
                navigationIcon = {
                    IconButton(onClick = {

                    })
                    {
                        Icon(Icons.Filled.Menu, contentDescription = "menu")
                    }

                },
                actions = {

                    IconButton(
                        onClick = {
                            viewModel.onEvent(NotesEvent.ToggleOrderSection)
                        },
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_sort_24),
                            contentDescription = "Sort notes"
                        )

                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
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

            BoxWithConstraints {
                val boxWidth = maxWidth * 0.7f
                val iconWidth = (maxWidth - boxWidth) / 3
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        modifier = Modifier.width(iconWidth),
                        onClick = {
                            navController.navigate(Screen.DeletedNotesScreen.route)
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Trash can")
                    }

                    IconButton(
                        modifier = Modifier.width(iconWidth),
                        onClick = {
                            navController.navigate(Screen.FavNotesScreen.route)
                        }
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "favorite notes")
                    }

                    IconButton(
                        modifier = Modifier.width(iconWidth),
                        onClick = { currentViewType = ViewType.LIST }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_list_24),
                            contentDescription = "List View"
                        )
                    }

                    IconButton(
                        modifier = Modifier.width(iconWidth),
                        onClick = { currentViewType = ViewType.GRID }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_grid_view_24),
                            contentDescription = "Grid View"
                        )
                    }

                }
            }

            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))

                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            if (state.notes.isEmpty()) {
                CircularProgressIndicator()
            } else {
                when (currentViewType) {
                    ViewType.GRID -> GridViewNotes(
                        navController = navController,
                        viewModel = viewModel,
                        scope = scope,
                        snackbarHostState = snackbarHostState,
                        notesList = state.notes,
                        context = context

                    )

                    ViewType.LIST -> ListViewNotes(
                        navController = navController,
                        viewModel = viewModel,
                        scope = scope,
                        snackbarHostState = snackbarHostState,
                        notesList = state.notes,
                        context = context
                    )
                }
            }

        }


    }
    }




