package com.john_halaka.mytodo.ui.todo_list

import com.john_halaka.noteTopia.feature_todo.domain.model.Todo


sealed class TodoListEvent {
    data class OnDeleteTodoClick(val todo: Todo) : TodoListEvent()
    data class OnCompletedChange(val todo: Todo, val isCompleted: Boolean) : TodoListEvent()
    object OnUndoDeleteClick : TodoListEvent()
    data class OnTodoClick(val todo: Todo) : TodoListEvent()
    object OnAddTodoClick : TodoListEvent()

}
