package mobappdev.example.todos.utils

import mobappdev.example.todos.data.todos.Todo

/**
 * Function that does some trivial computation. Used to showcase unit tests.
 */

internal fun getActiveAndCompletedStats(tasks: List<Todo>?): StatsResult {
    val totalTodos = tasks!!.size
    val numberOfActiveTodos = tasks.count { it.isActive }
    return StatsResult(
        activeTodosPercent = 100f * numberOfActiveTodos / tasks.size,
        completedTodosPercent = 100f * (totalTodos - numberOfActiveTodos) / tasks.size
    )
}

data class StatsResult(val activeTodosPercent: Float, val completedTodosPercent: Float)