package mobappdev.example.todos.ui.scr_and_vm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsScreen(
    vm: StatisticsViewModel
) {
    val completedTodosPercent = vm.completedTodosPercent.observeAsState()
    val activeTodosPercent = vm.activeTodosPercent.observeAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Statistics")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Completed todos: ${completedTodosPercent.value} %")
        Text(text = "Active todos: ${activeTodosPercent.value} %")
    }
}

@Preview
@Composable
fun StatisticsScreenPreview() {
    StatisticsScreen(
        vm = FakeStatsVM()
    )
}