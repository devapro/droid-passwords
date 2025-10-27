package io.github.devapro

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import io.github.devapro.droid.core.ui.AppSnackbarHost
import io.github.devapro.droid.welcome.navigation.WelcomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun AppContent() {
    Scaffold(
        snackbarHost = { AppSnackbarHost() },
        content = {
            Navigator(WelcomeScreen) { navigator ->
                SlideTransition(navigator)
            }
        }
    )
}