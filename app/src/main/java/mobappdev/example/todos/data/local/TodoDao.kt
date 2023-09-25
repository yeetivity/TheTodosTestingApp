package mobappdev.example.todos.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import mobappdev.example.todos.data.todos.Todo

@Dao
interface TodoDao {
    /**
     * Observes a list of Tasks
     * @return all tasks.
     */
    @Query("SELECT * FROM todos")
    fun observeTodos(): LiveData<List<Todo>>

    /**
     * Observes a single Task
     *
     * @param todoId the Task id.
     * @return the Task with the todoId
     */
    @Query("SELECT * FROM todos WHERE entryid = :todoId")
    fun observeTodoById(todoId: String): Flow<Todo>

    /**
     * Select all todos from the todos table
     * @return all tasks
     */
    @Query("SELECT * FROM todos")
    suspend fun getTodos(): List<Todo>

    /**
     * Select a task by id
     *
     * @param todoId the task id
     * @return the task with task id
     */
    @Query("SELECT * FROM todos WHERE entryid = :todoId")
    suspend fun getTodoById(todoId: String): Todo?

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param todo the Task to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    /**
     * Update a task.
     *
     * @param todo the task to be updated
     * @return the number of tasks updated. This should always be 1
     */
    @Update
    suspend fun updateTodo(todo: Todo): Int

    /**
     * Update the complete status of a task
     *
     * @param todoId id of the task
     * @param completed status to be updated
     */
    @Query("UPDATE todos SET completed = :completed WHERE entryid = :taskId")
    suspend fun updateCompleted(taskId: String, completed: Boolean)

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM todos")
    suspend fun deleteTodos()

    /**
     * Delete a task by id
     *
     * @param todoId id of the task to be deleted
     * @return number of tasks deleted. This should always be 1
     */
    @Query("DELETE FROM todos WHERE entryid = :todoId")
    suspend fun deleteTodoById(todoId: String): Int

    /**
     * Delete all completed tasks from the table
     *
     * @return the number of tasks deleted
     */
    @Query("DELETE FROM todos WHERE completed = 1")
    suspend fun deleteCompletedTasks(): Int

}