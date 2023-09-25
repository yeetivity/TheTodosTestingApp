package mobappdev.example.todos.ui.scr_and_vm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mobappdev.example.todos.data.todos.Todo
import mobappdev.example.todos.ui.components.AddNoteFAB
import mobappdev.example.todos.ui.navigation.AllDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoScreen(
    vm: TodoViewModel,
    navController: NavController
) {
    val selectedTodo = vm.selectedTodo.collectAsState().value
    var title by remember { mutableStateOf(selectedTodo?.title ?: "") }
    var description by remember { mutableStateOf(selectedTodo?.description ?: "") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AddNoteFAB(
                onClick = {
                    vm.deleteTodo(selectedTodo!!)
                    navController.navigate(AllDestinations.HOME)
                },
                icon = Icons.Filled.Delete,
                contentDescription = "Delete Todo"
            )
        }
    ) { paddingValues ->
        Column(

            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { value -> title = value },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = description,
                onValueChange = { value -> description = value },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.Black, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    vm.updateTodo(todo = Todo(title, description, selectedTodo!!.isCompleted, selectedTodo.id))
                    navController.navigate(AllDestinations.HOME)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text= "Update Todo")
            }
        }
    }
}

@Preview
@Composable
fun EditTodoPreview() {
    EditTodoScreen(
        vm = FakeTodoVM(),
        navController = rememberNavController()
    )
}