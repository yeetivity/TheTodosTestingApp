package mobappdev.example.todos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import mobappdev.example.todos.data.todos.Todo

/**
 * The Room Database that contains the todos table.
 *
 * Note that the exportSchema should be true in production databases
 */
@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}