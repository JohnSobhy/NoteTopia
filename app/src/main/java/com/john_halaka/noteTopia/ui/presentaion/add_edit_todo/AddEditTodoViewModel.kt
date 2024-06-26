package com.john_halaka.mytodo.ui.add_edit_todo

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_todo.domain.model.Todo
import com.john_halaka.noteTopia.feature_todo.domain.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val todoId = savedStateHandle.get<Int>("todoId")!!
        Log.d("AddEditTodoViewModel", "todoId: $todoId")
        if (todoId != -1) {
            viewModelScope.launch {
                repository.getTodoById(todoId)?.let { todo ->
                    title = todo.title
                    description = todo.description ?: ""
                    this@AddEditTodoViewModel.todo = todo
                }
            }
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.OnTitleChanged ->
                title = event.title

            is AddEditTodoEvent.OnDescriptionChanged ->
                description = event.description

            is AddEditTodoEvent.OnSaveTodoClicked -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackBar(
                                messageResId = R.string.the_title_can_t_be_empty,
                                actionResId = null
                            )
                        )
                        return@launch
                    }
                    repository.addTodo(
                        Todo(
                            title = title,
                            description = description,
                            completed = todo?.completed ?: false,
                            id = todo?.id
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
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
        object PopBackStack : UiEvent()
        data class ShowSnackBar(
            @StringRes val messageResId: Int,
            @StringRes val actionResId: Int? = null
        ) : UiEvent()
    }
}