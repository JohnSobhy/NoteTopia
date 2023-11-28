package com.john_halaka.mytodo

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.mytodo.ui.todo_list.TodoListEvent
import com.john_halaka.mytodo.ui.todo_list.TodoListViewModel
import com.john_halaka.noteTopia.NavigationDrawer
import com.john_halaka.noteTopia.NavigationItemsBar
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.ui.Screen
import com.john_halaka.noteTopia.ui.presentaion.util.findActivity
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoListScreen(
    navController: NavController,
    onNavigate: (TodoListViewModel.UiEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val todos = viewModel.todos.collectAsState(initial = emptyList())
    val scaffoldState = remember {
        SnackbarHostState()
    }
    val currentActivity = LocalContext.current.findActivity()
    val windowSize = calculateWindowSizeClass(activity = currentActivity)
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TodoListViewModel.UiEvent.ShowSnackBar -> {
                    val result = scaffoldState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }

                is TodoListViewModel.UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
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
                            Text(text = "To-do List")
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
                        })
                },
                bottomBar = {
                    NavigationItemsBar(navController = navController)
                },
                snackbarHost = {
                    SnackbarHost(scaffoldState)
                },
                floatingActionButton = {
                    FloatingActionButton(
                        shape = CircleShape,
                        onClick = {
                            navController.navigate(Screen.AddEditTodoScreen.route)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add a Todo")
                    }
                }

            ) { values ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(values)
                ) {
                    items(todos.value) { todo ->
                        TodoItem(
                            todo = todo,
                            onEvent = viewModel::onEvent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
                                }
                                .padding(16.dp)
                        )


                    }
                }
            }
        },
    )


}