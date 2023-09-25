package mobappdev.example.todos.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mobappdev.example.todos.data.local.TodoDatabase
import mobappdev.example.todos.data.local.TodoLocalDataSource
import mobappdev.example.todos.data.Result.*
import mobappdev.example.todos.data.todos.Todo

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 */
class TodoRepository private constructor(application: Application) {

    private val todosLocalDataSource: TodoDataSource
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    companion object {
        @Volatile
        private var INSTANCE: TodoRepository? = null

        fun getRepository(app: Application): TodoRepository {
            return INSTANCE ?: synchronized(this) {
                TodoRepository(app).also {
                    INSTANCE = it
                }
            }
        }
    }

    init {
        val database = Room.databaseBuilder(application.applicationContext,
            TodoDatabase::class.java, "Todos.db")
            .build()

        todosLocalDataSource = TodoLocalDataSource(database.todoDao())
    }

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
     * Relies on [getTasks] to fetch data and picks the task with the same ID.
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
