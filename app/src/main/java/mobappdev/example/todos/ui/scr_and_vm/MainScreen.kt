package mobappdev.example.todos.ui.scr_and_vm

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mobappdev.example.todos.R
import mobappdev.example.todos.ui.components.AppBar
import mobappdev.example.todos.ui.components.AppBarAction
import mobappdev.example.todos.ui.components.DrawerHeader
import mobappdev.example.todos.ui.navigation.AllDestinations
import mobappdev.example.todos.ui.navigation.AppNavigationActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: AllDestinations.HOME

    val todoViewModel = hiltViewModel<TodoVM>()
    val statsViewModel = hiltViewModel<StatisticsVM>()

    val navigationActions = remember(navController) {
        AppNavigationActions(navController)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(275.dp)
                    .fillMaxHeight()
            ) {
                DrawerHeader()
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Task List",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    selected = currentRoute == AllDestinations.HOME,
                    onClick = {
                        navigationActions.navigateToHome()
                        closeDrawer(coroutineScope, drawerState)
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = "Go to Task List") },
                    shape = MaterialTheme.shapes.small
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Statistics",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    selected = currentRoute == AllDestinations.STATISTICS,
                    onClick = {
                        navigationActions.navigateToStatistics()
                        closeDrawer(coroutineScope, drawerState)
                    },
                    icon = {
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_statistics),
                            contentDescription = "Go to Task List"
                        )
                    },
                    shape = MaterialTheme.shapes.small
                )
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true,
    ) {
        Scaffold(
            topBar = { AppBar(
                onNavigationIconClick = {
                    coroutineScope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                },
                actions = listOf(
                    // Todo: Add the other options
                    AppBarAction(
                        icon = ImageVector.vectorResource(R.drawable.ic_filter_list),
                        contentDescription = "Filter todos based on actions",
                        action = todoViewModel::cycleFiltering
                    ),
                    AppBarAction(
                        icon = Icons.Default.MoreVert,
                        contentDescription = "See more options",
                        action = { println("Seeing more options") }
                    )
                )
            ) }
        ) {
            NavHost(
                navController = navController,
                startDestination = AllDestinations.HOME,
                modifier = Modifier.padding(it)
            ) {
                composable(AllDestinations.HOME) {
                    TodoScreen(todoViewModel, navController)
                }
                composable(AllDestinations.STATISTICS) {
                    StatisticsScreen(statsViewModel)
                }
                composable(AllDestinations.TODO_ADD) {
                    AddTodoScreen(todoViewModel, navController)
                }
                composable(AllDestinations.TODO_EDIT) {
                    EditTodoScreen(todoViewModel, navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun closeDrawer(
    scope: CoroutineScope,
    drawerState: DrawerState,
) {
    scope.launch {
        drawerState.apply {
            close()
        }
    }
}