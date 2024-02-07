package com.john_halaka.noteTopia.ui.presentaion.notes_list

import android.content.Context
import android.util.Log
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
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.noteTopia.NavigationDrawer
import com.john_halaka.noteTopia.NavigationItemsBar
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_note.data.PreferencesManager
import com.john_halaka.noteTopia.feature_note.domain.util.ViewType
import com.john_halaka.noteTopia.ui.Screen
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.GridViewNotes
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.ListViewNotes
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.NotesViewDropDownItem
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.NotesViewDropDownMenu
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.SearchTextField
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.SmallGridViewNotes
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.SortDropDownMenu
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.mToast
import com.john_halaka.noteTopia.ui.presentaion.util.findActivity
import com.john_halaka.noteTopia.ui.theme.BabyBlue
import com.john_halaka.noteTopia.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
    context: Context
) {
    val state = viewModel.state.value
    var notesList = state.notes
    Log.d("NotesScreen", "state: $state")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val currentActivity = LocalContext.current.findActivity()
    val windowSize = calculateWindowSizeClass(activity = currentActivity)
    val focusManager = LocalFocusManager.current
    var searchText by remember { mutableStateOf("") }
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
                            Text(
                                text = stringResource(R.string.notes),
                                style = Typography.headlineLarge
                            )
                        },

                        navigationIcon = {
                            if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact)
                                IconButton(onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                })
                                {
                                    Icon(
                                        Icons.Filled.Menu,
                                        contentDescription = stringResource(R.string.menu)
                                    )
                                }
                            else {

                            }

                        },
                        actions = {
                            var expanded by remember { mutableStateOf(false) }
                            Box(
                            ) {
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


                            var viewMenuExpanded by remember { mutableStateOf(false) }

                            Box(
                            ) {

                                IconButton(
                                    onClick = {
                                        viewMenuExpanded = !viewMenuExpanded
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.GridView,
                                        contentDescription = "Change Notes view"
                                    )
                                }
                                NotesViewDropDownMenu(
                                    viewMenuExpanded = viewMenuExpanded,
                                    onDismiss = { viewMenuExpanded = false },
                                    viewType = currentViewType,
                                    onViewChanged = {
                                        currentViewType = it
                                    }
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    NavigationItemsBar(navController = navController)
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
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_a_note)
                        )
                    }
                },
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
                                        contentDescription = stringResource(R.string.search_icon),
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
                                        stringResource(R.string.find_in_your_notes),
                                        style = Typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            )
                        }

//                        AnimatedVisibility(
//                            visible = state.isOrderSectionVisible,
//                            enter = fadeIn() + slideInVertically(),
//                            exit = fadeOut() + slideOutVertically()
//                        ) {
//                            OrderSection(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 8.dp),
//                                noteOrder = state.noteOrder,
//                                onOrderChange = {
//                                     viewModel.onEvent(NotesEvent.Order(it))
//
//                                }
//                            )
//                        }

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
                                    Text(text = stringResource(R.string.add_your_first_note))
                            }
                        } else {
                            when (currentViewType) {
                                ViewType.GRID -> GridViewNotes(
                                    navController = navController,
                                    viewModel = viewModel,
                                    notesList = notesList,
                                    context = context,
                                    showFavoriteIcon = true

                                )

                                ViewType.LIST -> ListViewNotes(
                                    navController = navController,
                                    viewModel = viewModel,
                                    notesList = notesList,
                                    context = context,
                                    showFavoriteIcon = true
                                )

                                ViewType.SMALL_GRID -> SmallGridViewNotes(
                                    navController = navController,
                                    viewModel = viewModel,
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








