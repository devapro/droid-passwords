package io.github.devapro.features.edit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.core.ui.SnackbarHostStateManager
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenState
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPasswordScreenContent(
    state: AddEditPasswordScreenState.Success,
    onAction: (AddEditPasswordScreenAction) -> Unit
) {
    val snackBarManager: SnackbarHostStateManager = koinInject()
    Scaffold(
        topBar = {
            AddEditPasswordTopAppBar(
                state = state,
                onAction = onAction
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier,
                hostState = snackBarManager.state,
                snackbar = { snackbarData ->
                    Snackbar(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background),
                        snackbarData = snackbarData
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PasswordCoreFields(
                state = state,
                onAction = onAction
            )

            TagsSection(
                state = state,
                onAction = onAction
            )

            AdditionalFieldsSection(
                state = state,
                onAction = onAction
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Required fields note
            Text(
                text = "* Required fields",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
} 