package mobappdev.example.todos.ui.scr_and_vm

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsScreen(
    vm: StatisticsViewModel
) {
    LaunchedEffect(key1 = true) {
        vm.refresh()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Statistics")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Completed tasks: ${vm.completedTasksPercent.value}")
        Text(text = "Active tasks: ${vm.activeTasksPercent.value}")
    }
}

@Preview
@Composable
fun StatisticsScreenPreview() {
    StatisticsScreen(
        vm = FakeStatsVM()
    )
}