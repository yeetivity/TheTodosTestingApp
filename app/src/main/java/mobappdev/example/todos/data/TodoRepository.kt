package mobappdev.example.todos.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mobappdev.example.todos.data.Result.*
import mobappdev.example.todos.data.local.TodoLocalDataSource
import mobappdev.example.todos.data.todos.Todo
import javax.inject.Inject

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 */
class TodoRepository @Inject constructor(
    private val todosLocalDataSource: TodoLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher
) {


    suspend fun getTodos(forceUpdate: Boolean = false): Result<List<Todo>> {
        return todosLocalDataSource.getTodos()
    }

    fun observeTodos(): LiveData<Result<List<Todo>>> {
        return todosLocalDataSource.observeTodos()
    }

    fun observeTask(taskId: String): Flow<Result<Todo>> {
        return todosLocalDataSource.observeTodo(taskId)
    }

    /**
     * Relies on [getTodos] to fetch data and picks the task with the same ID.
     */
    suspend fun getTodo(todoId: String,  forceUpdate: Boolean = false): Result<Todo> {
        return todosLocalDataSource.getTodo(todoId)
    }

    suspend fun saveTodo(todo: Todo) {
        coroutineScope {
            launch { todosLocalDataSource.saveTodo(todo) }
        }
    }

    suspend fun updateTodo(todo: Todo) {
        coroutineScope {
            launch { todosLocalDataSource.updateTodo(todo) }
        }
    }

    suspend fun completeTodo(todo: Todo) {
        coroutineScope {
            launch { todosLocalDataSource.completeTodo(todo) }
        }
    }

    suspend fun completeTodo(todoId: String) {
        withContext(ioDispatcher) {
            (getTodoWithId(todoId) as? Success)?.let {
                completeTodo(it.data)
            }
        }
    }

    suspend fun activateTodo(task: Todo) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { todosLocalDataSource.activateTodo(task) }
        }
    }

    suspend fun activateTodo(taskId: String) {
        withContext(ioDispatcher) {
            (getTodoWithId(taskId) as? Success)?.let {
                activateTodo(it.data)
            }
        }
    }

    suspend fun clearCompletedTodos() {
        coroutineScope {
            launch { todosLocalDataSource.clearCompletedTodos() }
        }
    }

    suspend fun deleteAllTodos() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { todosLocalDataSource.deleteAllTodos() }
            }
        }
    }

    suspend fun deleteTodo(todoId: String) {
        coroutineScope {
            launch { todosLocalDataSource.deleteTodo(todoId) }
        }
    }

    private suspend fun getTodoWithId(id: String): Result<Todo> {
        return todosLocalDataSource.getTodo(id)
    }
}
