package io.github.devapro

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.github.devapro.ui.MainScreen
import io.github.devapro.ui.welcome.navigation.WelcomeScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(WelcomeScreen)
        //MainScreen()
    }
}