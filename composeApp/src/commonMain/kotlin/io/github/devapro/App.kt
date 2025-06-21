package io.github.devapro

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.github.devapro.ui.MainScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainScreen()
    }
}