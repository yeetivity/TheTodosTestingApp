package mobappdev.example.todos.dependency_injection

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import mobappdev.example.todos.data.TodoRepository
import mobappdev.example.todos.data.local.TodoDao
import mobappdev.example.todos.data.local.TodoDatabase
import mobappdev.example.todos.data.local.TodoLocalDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Application): TodoDatabase {
        return TodoDatabase.getTodoDatabase(context)
    }


    @Provides
    @Singleton
    fun provideLocalDao(db : TodoDatabase): TodoDao {
        return db.todoDao()
    }

    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideLocalDataSource(
        todoDao: TodoDao,
        ioDispatcher: CoroutineDispatcher
    ): TodoLocalDataSource {
        return TodoLocalDataSource(todoDao, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideTodoRepository(
        todoLocalDataSource: TodoLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ): TodoRepository {
        return TodoRepository(todoLocalDataSource, ioDispatcher)
    }
}