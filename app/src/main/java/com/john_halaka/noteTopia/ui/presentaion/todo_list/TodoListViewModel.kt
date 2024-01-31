package com.john_halaka.mytodo.ui.todo_list

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_todo.domain.model.Todo
import com.john_halaka.noteTopia.feature_todo.domain.repository.TodoRepository
import com.john_halaka.noteTopia.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val allTodos = repository.getAllTodos()
    val todos = allTodos.map { it.reversed() }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTodo: Todo? = null


    fun onEvent(event: TodoListEvent) {
        when (event) {
            is TodoListEvent.OnTodoClick -> {
                Log.d("TodoListViewModel", "OnTodoClick: TodoId= ${event.todo.id}")
                /*
                Note that the navigation argument SHOULD NOT contain any spaces (+ "?todoId=${event.todo.id}") or it won,t work
                 */

                sendUiEvent(UiEvent.Navigate(Screen.AddEditTodoScreen.route + "?todoId=${event.todo.id}"))
            }

            is TodoListEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Screen.AddEditTodoScreen.route))
            }

            is TodoListEvent.OnUndoDeleteClick -> {
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        repository.addTodo(todo)
                    }
                }

            }

            is TodoListEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(
                        UiEvent.ShowSnackBar(
                            messageResId = R.string.todo_deleted,
                            actionResId = R.string.undo
                        )
                    )
                }

            }

            is TodoListEvent.OnCompletedChange -> {
                viewModelScope.launch {
                    repository.addTodo(
                        event.todo.copy(
                            completed = event.isCompleted
                        )
                    )
                }
            }

            else -> {}
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    sealed class UiEvent {
        data class Navigate(val route: String) : UiEvent()
        data class ShowSnackBar(
            @StringRes val messageResId: Int,
            @StringRes val actionResId: Int? = null
        ) : UiEvent()

    }
}