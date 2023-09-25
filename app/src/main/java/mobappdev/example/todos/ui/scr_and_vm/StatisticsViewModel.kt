package mobappdev.example.todos.ui.scr_and_vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import mobappdev.example.todos.data.TodoRepository
import mobappdev.example.todos.data.Result.Success
import mobappdev.example.todos.data.Result.Error
import mobappdev.example.todos.utils.getActiveAndCompletedStats


interface StatisticsViewModel {
    val activeTasksPercent: LiveData<Float>
    val completedTasksPercent: LiveData<Float>

    fun refresh()
}

class StatisticsVM(
    application: Application
) : StatisticsViewModel, AndroidViewModel(application) {

    // Note, for testing and architecture purposes, it's bad practice to construct the repository
    // here. We'll show you how to fix this during the codelab
    private val todoRepository by lazy {
        TodoRepository.getRepository(application)
    }
    private val tasks by lazy {
        todoRepository.observeTodos()
    }

    private val stats by lazy {
        tasks.map {
            if (it is Success) {
                getActiveAndCompletedStats(it.data)
            } else {
                null
            }
        }
    }


    override val activeTasksPercent = stats.map {
        it?.activeTasksPercent ?: 0f }
    override val completedTasksPercent: LiveData<Float> = stats.map { it?.completedTasksPercent ?: 0f }
    val error: LiveData<Boolean> = tasks.map { it is Error }
    val empty: LiveData<Boolean> = tasks.map { (it as? Success)?.data.isNullOrEmpty() }

    override fun refresh() { }
}

class FakeStatsVM(): StatisticsViewModel {
    override val activeTasksPercent: LiveData<Float>
        get() = MutableLiveData(0f)
    override val completedTasksPercent: LiveData<Float>
        get() = MutableLiveData(0f)

    override fun refresh() { }
}



