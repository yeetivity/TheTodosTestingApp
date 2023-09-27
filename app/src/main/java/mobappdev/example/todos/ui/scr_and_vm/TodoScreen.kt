package mobappdev.example.todos.ui.scr_and_vm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mobappdev.example.todos.data.todos.Todo
import mobappdev.example.todos.ui.components.AddNoteFAB
import mobappdev.example.todos.ui.navigation.AllDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    vm: TodoViewModel,
    navController: NavController
) {
    val todoItems = vm.items.observeAsState().value
    val filterTextId = vm.currentFilteringLabel.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AddNoteFAB(
                onClick = { navController.navigate(AllDestinations.TODO_ADD) },
                icon = Icons.Filled.Add,
                contentDescription = "Add new Todo",
                modifier = Modifier.testTag("AddTodoFAB")
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ){
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = filterTextId),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ){
                items(todoItems?: emptyList()) {todo ->
                    TodoRow(
                        todo = todo,
                        onClick = {
                            vm.openTodo(todo = todo)
                            navController.navigate(AllDestinations.TODO_EDIT)
                        },
                        onCompleteChange = { vm.completeTodo(todo, !todo.isCompleted) },
                        modifier = Modifier.testTag(todo.title)
                    )
                }
            }
        }
    }
}



@Composable
fun TodoRow(
    todo: Todo,
    onClick: () -> Unit,
    onCompleteChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            modifier = Modifier.padding(4.dp),
            checked = todo.isCompleted,
            onCheckedChange = { onCompleteChange() }
        )
        Text(
            text = todo.titleForList,
            style = TextStyle(
                color = if (todo.isCompleted) Color.Gray else Color.Black
            )
        )
    }
}

@Preview
@Composable
fun TodoScreenPreview() {
    TodoScreen(
        vm = FakeTodoVM(),
        navController = rememberNavController()
    )
}