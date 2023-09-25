package mobappdev.example.todos.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import mobappdev.example.todos.data.todos.Todo

/**
 * Main blueprint for datasources
 */
interface TodoDataSource {
    fun observeTodos(): LiveData<Result<List<Todo>>>

    suspend fun getTodos(): Result<List<Todo>>

    suspend fun refreshTodos()

    fun observeTodo(todoId: String): Flow<Result<Todo>>

    suspend fun getTodo(todoId: String): Result<Todo>

    suspend fun refreshTodo(todoId: String)

    suspend fun saveTodo(todo: Todo)

    suspend fun completeTodo(todo: Todo)

    suspend fun updateTodo(todo: Todo)

    suspend fun completeTodo(todoId: String)

    suspend fun activateTodo(todo: Todo)

    suspend fun activateTodo(todoId: String)

    suspend fun clearCompletedTodos()

    suspend fun deleteAllTodos()

    suspend fun deleteTodo(todoId: String)
}