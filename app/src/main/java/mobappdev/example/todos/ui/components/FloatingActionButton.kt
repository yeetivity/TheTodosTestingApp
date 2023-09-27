package mobappdev.example.todos.ui.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import mobappdev.example.todos.ui.theme.Green
import mobappdev.example.todos.ui.theme.OffWhite

@Composable
fun AddNoteFAB(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = Green,
        contentColor = OffWhite,
        modifier = modifier
    ) {
        Icon(icon, contentDescription)
    }
}