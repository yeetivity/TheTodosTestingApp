package mobappdev.example.todos.ui.scr_and_vm

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mobappdev.example.todos.R
import mobappdev.example.todos.data.TodoRepository
import mobappdev.example.todos.data.Result
import mobappdev.example.todos.data.Result.Success
import mobappdev.example.todos.data.todos.Todo
import mobappdev.example.todos.data.todos.TodoFilterType
import mobappdev.example.todos.ui.components.Event
import mobappdev.example.todos.utils.Constants.ADD_EDIT_RESULT_OK
import mobappdev.example.todos.utils.Constants.DELETE_RESULT_OK
import mobappdev.example.todos.utils.Constants.EDIT_RESULT_OK

interface TodoViewModel {
    val items: LiveData<List<Todo>>
    val currentFilteringLabel: StateFlow<Int>
    val noTodosLabel: StateFlow<Int>
    val snackbarText: StateFlow<Event<Int>?>
    val empty: StateFlow<Boolean>
    val selectedTodo: StateFlow<Todo?>

    fun cycleFiltering()
    fun clearCompletedTodos()
    fun completeTodo(todo: Todo, completed: Boolean)
    fun addNewTodo(title: String, description: String)
    fun updateTodo(todo: Todo)
    fun openTodo(todo: Todo)
    fun deleteTodo(todo: Todo)
    fun showEditResultMessage(result: Int)
}

class TodoVM(
    application: Application
): TodoViewModel, AndroidViewModel(application) {
    // Todo: Note, for testing and architecture purposes, it's bad practice to construct the repository
    // here. We'll show you how to fix this during the codelab
    private val todoRepository = TodoRepository.getRepository(application)

    private val _filterCriteria: MutableLiveData<TodoFilterType> = MutableLiveData(TodoFilterType.ALL_TODOS)
    private val _mediatorItems = MediatorLiveData<List<Todo>>()
    override val items: LiveData<List<Todo>>
        get() = _mediatorItems

    private val _currentFilteringLabel = MutableStateFlow(R.string.label_all)
    override val currentFilteringLabel: StateFlow<Int>
        get() = _currentFilteringLabel.asStateFlow()

    private val _noTodosLabel = MutableStateFlow(R.string.no_tasks_all)
    override val noTodosLabel: StateFlow<Int>
        get() = _noTodosLabel.asStateFlow()

    private val _snackbarText = MutableStateFlow<Event<Int>?>(null)
    override val snackbarText: StateFlow<Event<Int>?>
        get() = _snackbarText.asStateFlow()


    private val _empty = MutableStateFlow(_mediatorItems.value?.isEmpty() ?: true)
    override val empty: StateFlow<Boolean>
        get() = _empty.asStateFlow()

    private val _selectedTodo: MutableStateFlow<Todo?> = MutableStateFlow(null)
    override val selectedTodo: StateFlow<Todo?>
        get() = _selectedTodo.asStateFlow()

    private var resultMessageShown: Boolean = false

    init {
        _mediatorItems.addSource(_filterCriteria) { filter ->
            _mediatorItems.removeSource(todoRepository.observeTodos())
            _mediatorItems.addSource(todoRepository.observeTodos()) {result ->
                _mediatorItems.value = filterItems(result, filter)
            }
        }
    }


    override fun cycleFiltering() {
        // Cycle through the filter types
        _filterCriteria.value = when (_filterCriteria.value) {
            TodoFilterType.ALL_TODOS -> TodoFilterType.ACTIVE_TODOS
            TodoFilterType.ACTIVE_TODOS -> TodoFilterType.COMPLETED_TODOS
            TodoFilterType.COMPLETED_TODOS -> TodoFilterType.ALL_TODOS
            else -> TodoFilterType.ALL_TODOS
        }

        // Depending on the filter type, set the filtering label, icon drawables, etc.
        when (_filterCriteria.value) {
            TodoFilterType.ALL_TODOS -> { setFilter(R.string.label_all, R.string.no_tasks_all) }
            TodoFilterType.ACTIVE_TODOS -> { setFilter(R.string.label_active, R.string.no_tasks_active) }
            TodoFilterType.COMPLETED_TODOS -> { setFilter(R.string.label_completed, R.string.no_tasks_completed) }
            else -> {}
        }
    }

    private fun setFilter(
        @StringRes filteringLabelString: Int, @StringRes noTodosLabelString: Int
    ) {
        _currentFilteringLabel.update { filteringLabelString }
        _noTodosLabel.update { noTodosLabelString }
    }

    override fun clearCompletedTodos() {
        viewModelScope.launch {
            todoRepository.clearCompletedTodos()
            showSnackbarMessage(R.string.completed_todos_cleared)
        }
    }

    override fun completeTodo(todo: Todo, completed: Boolean) {
        viewModelScope.launch {
            if (completed) {
                todoRepository.completeTodo(todo)
                showSnackbarMessage(R.string.todo_marked_as_complete)
            } else {
                todoRepository.activateTodo(todo)
                showSnackbarMessage(R.string.todo_marked_active)
            }
        }
    }

    override fun addNewTodo(title: String, description: String) {
        viewModelScope.launch {
            todoRepository.saveTodo(
                Todo(title = title, description = description)
            )
        }
    }

    override fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.updateTodo(todo)
        }
    }

    override fun openTodo(todo: Todo) {
        _selectedTodo.update { todo }
    }

    override fun showEditResultMessage(result: Int) {
        if (resultMessageShown) return
        when (result) {
            EDIT_RESULT_OK -> showSnackbarMessage(R.string.todo_saved)
            ADD_EDIT_RESULT_OK -> showSnackbarMessage(R.string.todo_added)
            DELETE_RESULT_OK -> showSnackbarMessage(R.string.todo_deleted)
        }
        resultMessageShown = true
    }

    override fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo.id)
            showEditResultMessage(DELETE_RESULT_OK)
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

//    private fun filterTodos(todoResult: Result<List<Todo>>, filter: TodoFilterType): LiveData<List<Todo>> {
//        // Todo: this is a good case for a flow builder? Replace when stable
//        val result = if (todoResult is Success) {
//            viewModelScope.launch {
//                filterItems(todoResult.data, filter)
//            }
//        } else {
//            emptyList()
//            showSnackbarMessage(R.string.error_loading_todos)
//        }
//        return result
//    }

    private fun filterItems(todoResult: Result<List<Todo>>, filteringType: TodoFilterType): List<Todo> {
        val todosToShow = ArrayList<Todo>()

        if (todoResult is Success){
            for (todo in todoResult.data) {
                when (filteringType) {
                    TodoFilterType.ALL_TODOS -> todosToShow.add(todo)
                    TodoFilterType.ACTIVE_TODOS -> if (todo.isActive) {
                        todosToShow.add(todo)
                    }
                    TodoFilterType.COMPLETED_TODOS -> if (todo.isCompleted) {
                        todosToShow.add(todo)
                    }
                }
            }
        } else {
            showSnackbarMessage(R.string.error_loading_todos)
        }

        return todosToShow
    }
}

class FakeTodoVM : TodoViewModel {
    override val items: LiveData<List<Todo>>
        get() = MutableLiveData(
            listOf(
                Todo(
                    title = "Something 1",
                    description = "This is my descr",
                    isCompleted = false
                ),
                Todo(
                    title = "Something 2",
                    description = "This is my descr",
                    isCompleted = true
                ),
                Todo(
                    title = "Something 3",
                    description = "This is my descr",
                    isCompleted = true
                ),
                Todo(
                    title = "Something 4",
                    description = "This is my descr",
                    isCompleted = false
                )
            )
        )

    override val currentFilteringLabel: StateFlow<Int>
        get() =  MutableStateFlow(R.string.label_all).asStateFlow()

    override val noTodosLabel: StateFlow<Int>
        get() = MutableStateFlow(R.string.no_tasks_all)

    override val snackbarText: StateFlow<Event<Int>?>
        get() = MutableStateFlow<Event<Int>?>(null)

    override val empty: StateFlow<Boolean>
        get() = MutableStateFlow(items.value?.isEmpty() ?: true).asStateFlow()

    override val selectedTodo: StateFlow<Todo?>
        get() = MutableStateFlow(Todo("Some title", "Some description")).asStateFlow()

    override fun cycleFiltering() { }

    override fun clearCompletedTodos() { }

    override fun completeTodo(todo: Todo, completed: Boolean) { }

    override fun addNewTodo(title: String, description: String) { }

    override fun openTodo(todo: Todo) { }

    override fun updateTodo(todo: Todo) { }

    override fun showEditResultMessage(result: Int) { }

    override fun deleteTodo(todo: Todo) { }
}