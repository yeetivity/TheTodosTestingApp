package mobappdev.example.todos.data.todos

/**
 * Used with the filter spinner in the tasks list
 */
enum class TodoFilterType {
    /**
     * Do not filter tasks
     */
    ALL_TODOS,

    /**
     * Filters only the active (not yet completed) todos
     */
    ACTIVE_TODOS,

    /**
     * Filters only the completed tasks
     */
    COMPLETED_TODOS
}