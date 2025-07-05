package io.github.devapro.features.edit.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPasswordTopAppBar(
    state: AddEditPasswordScreenState.Success,
    onAction: (AddEditPasswordScreenAction) -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = if (state.isEditMode) "Edit Password" else "Add Password",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { onAction(AddEditPasswordScreenAction.OnBackClicked) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            if (state.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(end = 16.dp)
                )
            } else {
                IconButton(
                    onClick = { onAction(AddEditPasswordScreenAction.OnSaveClicked) },
                    enabled = state.isFormValid
                ) {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = "Save",
                        tint = if (state.isFormValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.38f
                        )
                    )
                }
            }
            if (state.isEditMode) {
                IconButton(onClick = { onAction(AddEditPasswordScreenAction.OnDeleteClicked) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    )
}