package com.john_halaka.notes.feature_todo.data.data_source

import androidx.room.*
import com.john_halaka.notes.feature_todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table")
    fun getAllTodos(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("SELECT * FROM todo_table WHERE id =:id")
    suspend fun getTodoById(id: Int): Todo?

//    @Update
//    fun updateTodo(todo: Todo)
}