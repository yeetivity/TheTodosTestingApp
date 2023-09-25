package mobappdev.example.todos.ui.navigation

import androidx.navigation.NavHostController
import mobappdev.example.todos.ui.navigation.AllDestinations.HOME
import mobappdev.example.todos.ui.navigation.AllDestinations.STATISTICS
import mobappdev.example.todos.ui.navigation.AllDestinations.TODO_ADD
import mobappdev.example.todos.ui.navigation.AllDestinations.TODO_EDIT

object AllDestinations {
    const val HOME = "home"
    const val STATISTICS = "statistics"
    const val TODO_EDIT = "todo_edit"
    const val TODO_ADD = "todo_add"
}

class AppNavigationActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(HOME) {
            popUpTo(HOME)
        }
    }

    fun navigateToStatistics() {
        navController.navigate(STATISTICS) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToTodoDetail() {
        navController.navigate(TODO_EDIT) {
            launchSingleTop = true
            restoreState = false
        }
    }

    fun navigateToAddTodo() {
        navController.navigate(TODO_ADD) {
            launchSingleTop = true
            restoreState = false
        }
    }
}