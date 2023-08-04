package com.john_halaka.notes.feature_todo.domain.repository

import com.john_halaka.notes.feature_todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun getAllTodos() : Flow<List<Todo>>
    suspend fun addTodo(todo: Todo)
    suspend fun deleteTodo(todo: Todo)
    suspend fun getTodoById(id: Int) : Todo?
}