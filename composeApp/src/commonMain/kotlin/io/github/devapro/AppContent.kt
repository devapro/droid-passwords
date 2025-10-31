package io.github.devapro

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import io.github.devapro.droid.core.navigation.LocalWideScreenFlag
import io.github.devapro.droid.core.ui.AppSnackbarHost
import io.github.devapro.droid.welcome.navigation.WelcomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun AppContent() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp

        CompositionLocalProvider(LocalWideScreenFlag provides isWideScreen) {
            Scaffold(
                snackbarHost = { AppSnackbarHost() },
                content = {
                    Navigator(WelcomeScreen) { navigator ->
                        SlideTransition(navigator)
                    }
                }
            )
        }
    }
}