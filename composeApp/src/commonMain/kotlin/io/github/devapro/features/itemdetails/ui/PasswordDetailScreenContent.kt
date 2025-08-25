package io.github.devapro.features.itemdetails.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.core.ui.SnackbarHostStateManager
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenState
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordDetailScreenContent(
    state: PasswordDetailScreenState.Success,
    onAction: (PasswordDetailScreenAction) -> Unit
) {
    val snackBarManager: SnackbarHostStateManager = koinInject()
    Scaffold(
        topBar = {
            PasswordDetailTopAppBar(
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
            // Title
            DetailField(
                label = "Title",
                value = state.item.title,
                onCopy = { onAction(PasswordDetailScreenAction.OnCopyField("Title", state.item.title)) }
            )

            // Username
            if (state.item.username.isNotEmpty()) {
                DetailField(
                    label = "Username",
                    value = state.item.username,
                    onCopy = { onAction(PasswordDetailScreenAction.OnCopyField("Username", state.item.username)) }
                )
            }

            // Password
            PasswordField(
                value = state.item.password,
                isVisible = state.isPasswordVisible,
                onToggleVisibility = { onAction(PasswordDetailScreenAction.OnTogglePasswordVisibility) },
                onCopy = { onAction(PasswordDetailScreenAction.OnCopyField("Password", state.item.password)) }
            )

            // URL
            if (state.item.url.isNotEmpty()) {
                DetailField(
                    label = "URL",
                    value = state.item.url,
                    onCopy = { onAction(PasswordDetailScreenAction.OnCopyField("URL", state.item.url)) }
                )
            }

            // Description
            if (state.item.description.isNotEmpty()) {
                DetailField(
                    label = "Description",
                    value = state.item.description,
                    onCopy = { onAction(PasswordDetailScreenAction.OnCopyField("Description", state.item.description)) },
                    isMultiline = true
                )
            }

            // Tags
            TagsSection(tags = state.item.tags)

            // Additional Fields
            if (state.item.additionalFields.isNotEmpty()) {
                Text(
                    text = "Additional Fields",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                state.item.additionalFields.forEach { field ->
                    DetailField(
                        label = field.name,
                        value = field.value,
                        onCopy = { onAction(PasswordDetailScreenAction.OnCopyField(field.name, field.value)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Delete confirmation dialog
    if (state.showDeleteConfirmation) {
        DeleteConfirmationDialog(
            onAction = onAction
        )
    }
} 