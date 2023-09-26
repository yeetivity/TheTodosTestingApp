package mobappdev.example.todos.statistics

import mobappdev.example.todos.data.todos.Todo
import mobappdev.example.todos.utils.getActiveAndCompletedStats
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class StatisticsTest {

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {
        // Create an active task
        val tasks = listOf(
            Todo("title", "desc", isCompleted = false)
        )

        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        MatcherAssert.assertThat(result.completedTodosPercent, CoreMatchers.equalTo(0f))
        MatcherAssert.assertThat(result.activeTodosPercent, CoreMatchers.equalTo(100f))
    }

    @Test
    fun getActiveAndCompletedStats_noActive_returnsZeroHundred() {
        // Create an active task
        val tasks = listOf(
            Todo("title", "desc", isCompleted = true)
        )

        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        MatcherAssert.assertThat(result.completedTodosPercent, CoreMatchers.equalTo(100f))
        MatcherAssert.assertThat(result.activeTodosPercent, CoreMatchers.equalTo(0f))
    }

    @Test
    fun getActiveAndCompletedStats_both_returnsFortySixty() {
        val tasks = listOf(
            Todo("title", "desc", isCompleted = true),
            Todo("title", "desc", isCompleted = true),
            Todo("title", "desc", isCompleted = true),
            Todo("title", "desc", isCompleted = false),
            Todo("title", "desc", isCompleted = false),
        )

        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        MatcherAssert.assertThat(result.completedTodosPercent, CoreMatchers.equalTo(60f))
        MatcherAssert.assertThat(result.activeTodosPercent, CoreMatchers.equalTo(40f))
    }

    @Test
    fun getActiveAndCompletedStats_error_returnsZeros() {
        // When there's an error loading stats
        val result = getActiveAndCompletedStats(null)

        // Both active and completed tasks are 0
        MatcherAssert.assertThat(result.activeTodosPercent, CoreMatchers.`is`(0f))
        MatcherAssert.assertThat(result.completedTodosPercent, CoreMatchers.`is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZeros() {
        // When there are no tasks
        val result = getActiveAndCompletedStats(emptyList())

        // Both active and completed tasks are 0
        MatcherAssert.assertThat(result.activeTodosPercent, CoreMatchers.`is`(0f))
        MatcherAssert.assertThat(result.completedTodosPercent, CoreMatchers.`is`(0f))
    }
}