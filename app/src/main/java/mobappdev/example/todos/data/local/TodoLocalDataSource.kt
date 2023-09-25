package mobappdev.example.todos.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import mobappdev.example.todos.data.todos.Todo
import mobappdev.example.todos.data.TodoDataSource
import mobappdev.example.todos.data.Result
import mobappdev.example.todos.data.Result.Success
import mobappdev.example.todos.data.Result.Error
import javax.inject.Inject

// Todo: Set up the Dependency Injection
class TodoLocalDataSource @Inject constructor(
    private val todoDao: TodoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): TodoDataSource {
    override fun observeTodos(): LiveData<Result<List<Todo>>> {
        return todoDao.observeTodos().map {
            Success(it)
        }
    }

    override fun observeTodo(todoId: String): Flow<Result<Todo>> {
        return todoDao.observeTodoById(todoId).map {
            Success(it)
        }
    }

    override suspend fun refreshTodos() {
        // No-OP
    }

    override suspend fun refreshTodo(todoId: String) {
        // No-OP
    }

    override suspend fun getTodos(): Result<List<Todo>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(todoDao.getTodos())
        } catch (e: Exception) {
            return@withContext Error(e)
        }
    }

    override suspend fun getTodo(todoId: String): Result<Todo> = withContext(ioDispatcher) {
        try {
            val todo = todoDao.getTodoById(todoId)
            if (todo != null) {
                return@withContext Success(todo)
            } else {
                return@withContext Error(Exception("Todo not found!"))
            }
        } catch (e: Exception) {
            return@withContext Error(e)
        }
    }

    override suspend fun saveTodo(todo: Todo) = withContext(ioDispatcher) {
        todoDao.insertTodo(todo)
    }

    override suspend fun completeTodo(todo: Todo) {
        todoDao.updateCompleted(todo.id, true)
    }

    override suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo)
    }

    override suspend fun completeTodo(todoId: String) {
        todoDao.updateCompleted(todoId, true)
    }

    override suspend fun activateTodo(todo: Todo) = withContext(ioDispatcher) {
        todoDao.updateCompleted(todo.id, false)
    }

    override suspend fun activateTodo(todoId: String) {
        todoDao.updateCompleted(todoId, false)
    }

    override suspend fun clearCompletedTodos() = withContext<Unit>(ioDispatcher){
        todoDao.deleteCompletedTasks()
    }

    override suspend fun deleteAllTodos() = withContext(ioDispatcher) {
        todoDao.deleteTodos()
    }

    override suspend fun deleteTodo(todoId: String) = withContext<Unit>(ioDispatcher) {
        todoDao.deleteTodoById(todoId)
    }
}