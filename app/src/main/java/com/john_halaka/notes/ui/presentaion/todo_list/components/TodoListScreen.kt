package com.john_halaka.mytodo

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.mytodo.ui.todo_list.TodoListEvent
import com.john_halaka.mytodo.ui.todo_list.TodoListViewModel
import com.john_halaka.notes.BottomNavigationBar


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoListScreen (
    navController: NavController,
    onNavigate : (TodoListViewModel.UiEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
){
    val todos = viewModel.todos.collectAsState(initial = emptyList())
    val scaffoldState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {event ->
            when (event) {
                is TodoListViewModel.UiEvent.ShowSnackBar -> {
                  val result =  scaffoldState.showSnackbar(
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
    Scaffold (
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        snackbarHost = {
           SnackbarHost(scaffoldState)
        },

            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(todos.value) { todo ->
                        TodoItem(
                            todo = todo ,
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

}