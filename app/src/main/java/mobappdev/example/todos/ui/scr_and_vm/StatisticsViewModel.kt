package mobappdev.example.todos.ui.scr_and_vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import mobappdev.example.todos.data.Result.Success
import mobappdev.example.todos.data.TodoRepository
import mobappdev.example.todos.utils.StatsResult
import mobappdev.example.todos.utils.getActiveAndCompletedStats
import javax.inject.Inject


interface StatisticsViewModel {
    val activeTodosPercent: LiveData<Float>
    val completedTodosPercent: LiveData<Float>

    fun refresh()
}

@HiltViewModel
class StatisticsVM @Inject constructor(
    private val todoRepository: TodoRepository
) : StatisticsViewModel, ViewModel() {

    private val todosLiveData = todoRepository.observeTodos()
    private val statsLiveData: LiveData<StatsResult?> = todosLiveData.map {result ->
        if (result is Success) {
            getActiveAndCompletedStats(result.data)
        } else {
            null
        }
    }

    override val activeTodosPercent = statsLiveData.map { statsResult ->
        statsResult?.activeTodosPercent ?: 0f
    }
    override val completedTodosPercent = statsLiveData.map { statsResult ->
        statsResult?.completedTodosPercent ?: 0f
    }

    override fun refresh() { }
}

class FakeStatsVM(): StatisticsViewModel {
    override val activeTodosPercent: LiveData<Float>
        get() = MutableLiveData(0f)
    override val completedTodosPercent: LiveData<Float>
        get() = MutableLiveData(0f)

    override fun refresh() { }
}



