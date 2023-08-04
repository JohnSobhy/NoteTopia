package com.john_halaka.notes.feature_todo.data.repository


import com.john_halaka.notes.feature_todo.data.data_source.TodoDao
import com.john_halaka.notes.feature_todo.domain.model.Todo
import com.john_halaka.notes.feature_todo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl (
    private val todoDao: TodoDao
    ) : TodoRepository {
    override fun getAllTodos(): Flow<List<Todo>> {
        return todoDao.getAllTodos()
    }

    override suspend fun addTodo(todo: Todo) {
        todoDao.addTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo)
    }

    override suspend fun getTodoById(id: Int): Todo? {
        return todoDao.getTodoById(id)
    }

}