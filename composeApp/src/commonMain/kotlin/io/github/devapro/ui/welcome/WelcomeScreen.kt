package io.github.devapro.ui.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.devapro.ui.welcome.ui.CreateNewVault
import io.github.devapro.ui.welcome.ui.ImportExistingVault
import io.github.devapro.ui.welcome.ui.WelcomeScreenContent
import org.koin.compose.koinInject

@Composable
fun WelcomeScreen() {
    val viewModel: WelcomeScreenViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    WelcomeScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
} 