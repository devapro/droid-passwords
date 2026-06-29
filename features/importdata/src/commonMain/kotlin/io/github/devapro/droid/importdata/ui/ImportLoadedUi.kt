package io.github.devapro.droid.importdata.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenState
import io.github.devapro.droid.importdata.model.ImportTarget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportLoadedUi(
    state: ImportScreenState.Loaded,
    onAction: (ImportScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Import Passwords") },
                navigationIcon = {
                    IconButton(onClick = { onAction(ImportScreenAction.OnBackClicked) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (state.canMergeIntoActive) {
                ImportTargetCard(
                    selected = state.target,
                    activeVaultName = state.activeVaultName,
                    onSelected = { onAction(ImportScreenAction.OnTargetSelected(it)) },
                )
            } else {
                Text(
                    text = "No active vault is unlocked — imports will create a new vault.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            FormatSelectionCard(
                state = state,
                onFormatSelected = { format ->
                    onAction(ImportScreenAction.OnFormatSelected(format))
                }
            )

            Button(
                onClick = { onAction(ImportScreenAction.OnImportClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isProcessing,
                contentPadding = PaddingValues(16.dp)
            ) {
                if (state.isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(imageVector = Icons.Default.Upload, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Choose ${state.selectedFormat.name} file",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = when (state.target) {
                    ImportTarget.MERGE_INTO_ACTIVE ->
                        "You'll review counts and pick a merge strategy before anything is written."
                    ImportTarget.NEW_VAULT ->
                        "You'll be asked to name and password-protect the new vault before it's saved."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // Password dialog for DATA files (decryption step)
        if (state.pendingFile != null && !state.isConfirmDialogVisible) {
            ImportPasswordDialog(state = state, onAction = onAction)
        }

        if (state.isConfirmDialogVisible) {
            ImportConfirmDialog(state = state, onAction = onAction)
        }
    }
}
