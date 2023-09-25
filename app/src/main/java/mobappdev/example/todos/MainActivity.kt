package mobappdev.example.todos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import mobappdev.example.todos.ui.scr_and_vm.MainScreen
import mobappdev.example.todos.ui.theme.TodosTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodosTheme {
                MainScreen(this.application)
            }
        }
    }
}