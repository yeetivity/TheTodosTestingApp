package mobappdev.example.todos.dependency_injection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mobappdev.example.todos.data.local.TodoDao
import mobappdev.example.todos.data.local.TodoDatabase
import mobappdev.example.todos.utils.Constants
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//object RepoModule {
//
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
//        return Room.databaseBuilder(
//            context,
//            TodoDatabase::class.java,
//            Constants.TODO_DATABASE_NAME
//        )
//            .allowMainThreadQueries()
//            .fallbackToDestructiveMigration()
//            .build()
//    }
//
//
//    @Provides
//    @Singleton
//    fun provideLocalDao(db : TodoDatabase): TodoDao {
//        return db.todoDao()
//    }
//}