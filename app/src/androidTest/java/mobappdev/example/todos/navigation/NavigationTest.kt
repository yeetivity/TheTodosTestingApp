package mobappdev.example.todos.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import mobappdev.example.todos.ui.navigation.AllDestinations
import mobappdev.example.todos.ui.scr_and_vm.FakeStatsVM
import mobappdev.example.todos.ui.scr_and_vm.FakeTodoVM
import mobappdev.example.todos.ui.scr_and_vm.MainNavHost
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: NavController

    @Before
    fun setUpNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            
            MainNavHost(
                navController = navController as TestNavHostController,
                startDestination = AllDestinations.HOME,
                todoViewModel = FakeTodoVM(),
                statsViewModel = FakeStatsVM(),
            )
        }
    }

    @Test
    fun verify_StartDestinationIsHomeScreen() {
        composeTestRule
            .onNodeWithText("All Tasks")
            .assertIsDisplayed()
    }

    @Test
    fun verify_AddTodoDestinationIsAddTodoScreen() {
        composeTestRule
            .onNodeWithTag("AddTodoFAB")
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        Assert.assertEquals(route, AllDestinations.TODO_ADD)
    }

    @Test
    fun verify_TodoClickedDestinationIsDetailsScreen() {
        // I'll test to click one of the todos I created in FakeVM.

        composeTestRule
            .onNodeWithTag("Todo 1")
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        Assert.assertEquals(route, AllDestinations.TODO_EDIT)
    }
}