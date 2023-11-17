package com.john_halaka.noteTopia.ui.presentaion.notes_list

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.noteTopia.BottomNavigationBar
import com.john_halaka.noteTopia.NavigationDrawer
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_note.data.PreferencesManager
import com.john_halaka.noteTopia.feature_note.domain.util.ViewType
import com.john_halaka.noteTopia.ui.Screen
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.GridViewNotes
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.ListViewNotes
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.OrderSection
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.SearchTextField
import com.john_halaka.noteTopia.ui.theme.BabyBlue
import com.john_halaka.noteTopia.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
    context: Context
) {
    val state = viewModel.state.value
    // val state by viewModel.state.collectAsState()

    Log.d("NotesScreen", "state: $state")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    // var currentViewType by remember { mutableStateOf(ViewType.GRID) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var notesList = state.notes
    val focusManager = LocalFocusManager.current

    val preferencesManager = remember { PreferencesManager(context) }
    var currentViewType by remember {
        mutableStateOf(
            ViewType.valueOf(
                preferencesManager.getString(
                    "viewPreference",
                    ViewType.GRID.name
                )
            )
        )
    }
    LaunchedEffect(currentViewType) {
        preferencesManager.saveString("viewPreference", currentViewType.name)
    }

    NavigationDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope,
        content = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(

                        title = {
                            Text(text = "Notes", style = Typography.headlineLarge)
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            })
                            {
                                Icon(Icons.Filled.Menu, contentDescription = "menu")
                            }

                        },
                        actions = {
                            if (currentViewType.name == "GRID") {
                                IconButton(onClick = {
                                    currentViewType = ViewType.LIST
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.List,
                                        contentDescription = "change Notes view to List"
                                    )
                                }
                            } else {
                                IconButton(onClick = {
                                    currentViewType = ViewType.GRID
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_grid_view_24),
                                        contentDescription = "change Notes view to Grid"
                                    )
                                }

                            }
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
                },
                floatingActionButton = {
                    FloatingActionButton(
                        shape = CircleShape,
                        onClick = {
                            navController.navigate(Screen.AddEditNoteScreen.route)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add a note")
                    }
                }
            ) { values ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { focusManager.clearFocus() })
                        }
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(values)

                    ) {

//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.End,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            IconButton(
//                                onClick = { currentViewType = ViewType.LIST }
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.List,
//                                    contentDescription = "List View"
//                                )
//                            }
//
//                            IconButton(
//                                onClick = { currentViewType = ViewType.GRID }
//                            ) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.baseline_grid_view_24),
//                                    contentDescription = "Grid View"
//                                )
//                            }
//
//                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            SearchTextField(
                                value = searchText,
                                onValueChange = { searchPhrase ->
                                    viewModel.onEvent(NotesEvent.SearchNotes(searchPhrase))
                                    notesList = state.searchResult
                                    searchText = searchPhrase
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search Icon",
                                        modifier = Modifier.clickable(
                                            onClick = {
                                                viewModel.onEvent(NotesEvent.SearchNotes(searchText))
                                                notesList = state.searchResult
                                            }
                                        ),
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                },
                                placeholder = {
                                    Text(
                                        "Find in your notes",
                                        style = Typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            )
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

                        Spacer(modifier = Modifier.height(8.dp))


                        if (state.notes.isEmpty()) {
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
                                    Text(text = "Add your first Note")
                            }
                        } else {
                            when (currentViewType) {
                                ViewType.GRID -> GridViewNotes(
                                    navController = navController,
                                    viewModel = viewModel,
                                    scope = scope,
                                    snackbarHostState = snackbarHostState,
                                    notesList = notesList,
                                    context = context,
                                    showFavoriteIcon = true

                                )

                                ViewType.LIST -> ListViewNotes(
                                    navController = navController,
                                    viewModel = viewModel,
                                    scope = scope,
                                    snackbarHostState = snackbarHostState,
                                    notesList = notesList,
                                    context = context,
                                    showFavoriteIcon = true
                                )
                            }
                        }

                    }
                }
            }
        }
    )
}








