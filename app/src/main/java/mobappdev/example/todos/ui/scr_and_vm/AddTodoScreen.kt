package mobappdev.example.todos.ui.scr_and_vm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mobappdev.example.todos.ui.components.AddNoteFAB
import mobappdev.example.todos.ui.navigation.AllDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen (
    vm: TodoViewModel,
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AddNoteFAB(
                onClick = {
                    vm.addNewTodo(title = title, description = description)
                    navController.navigate(AllDestinations.HOME)
                },
                icon = Icons.Filled.Check,
                contentDescription = "Save Todo"
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
        }
    }
}

@Preview
@Composable
fun AddTodoPreview() {
    AddTodoScreen(
        vm = FakeTodoVM(),
        navController = rememberNavController()
    )
}
