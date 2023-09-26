package mobappdev.example.todos.utils

import mobappdev.example.todos.data.todos.Todo

/**
 * Function that does some trivial computation. Used to showcase unit tests.
 */

internal fun getActiveAndCompletedStats(todos: List<Todo>?): StatsResult {
    return if (todos.isNullOrEmpty()) {
        StatsResult(0f, 0f)
    } else {
        val totalTodos = todos.size
        val numberOfActiveTodos = todos.count { it.isActive }
        StatsResult(
            activeTodosPercent = 100f * numberOfActiveTodos / todos.size,
            completedTodosPercent = 100f * (totalTodos - numberOfActiveTodos) / todos.size
        )
    }
}

data class StatsResult(val activeTodosPercent: Float, val completedTodosPercent: Float)