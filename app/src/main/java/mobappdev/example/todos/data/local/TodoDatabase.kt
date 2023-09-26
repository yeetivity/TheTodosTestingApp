package mobappdev.example.todos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mobappdev.example.todos.data.todos.Todo
import mobappdev.example.todos.utils.Constants

/**
 * The Room Database that contains the todos table.
 *
 * Note that the exportSchema should be true in production databases
 */
@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var Instance: TodoDatabase? = null

        fun getTodoDatabase(context: Context): TodoDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    TodoDatabase::class.java,
                    Constants.TODO_DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}