package io.github.devapro.droid.edit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import io.github.devapro.droid.core.ui.EOutlinedTextField
import io.github.devapro.droid.data.vault.VaultItemTag
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsSection(
    state: AddEditPasswordScreenState.Success,
    onAction: (AddEditPasswordScreenAction) -> Unit
) {
    val suggestedTags = state.allTags.filter {
        it.title.contains(state.tagInput, ignoreCase = true) && !state.tags.contains(it)
    }

    var dismissedByUser by remember { mutableStateOf(false) }

    LaunchedEffect(state.tagInput) {
        dismissedByUser = false
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        EOutlinedTextField(
            value = state.tagInput,
            onValueChange = { onAction(AddEditPasswordScreenAction.OnTagInputChanged(it)) },
            label = { Text("Tags") },
            forceUpdate = true,
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { event ->
                    if (event.key == Key.Escape && event.type == KeyEventType.KeyDown) {
                        dismissedByUser = true
                        true
                    } else {
                        false
                    }
                }
        )

        DropdownMenu(
            expanded = !dismissedByUser && suggestedTags.isNotEmpty() && state.tagInput.isNotBlank(),
            onDismissRequest = { dismissedByUser = true },
            properties = PopupProperties(focusable = false),
            modifier = Modifier.fillMaxWidth()
        ) {
            suggestedTags.forEach { tag ->
                DropdownMenuItem(
                    text = { Text(tag.title) },
                    onClick = { onAction(AddEditPasswordScreenAction.OnTagSelected(tag)) }
                )
            }
            if (state.tagInput.isNotBlank() && suggestedTags.none {
                    it.title.equals(
                        state.tagInput,
                        ignoreCase = true
                    )
                }) {
                DropdownMenuItem(
                    text = { Text("Add \"${state.tagInput}\"") },
                    onClick = {
                        onAction(
                            AddEditPasswordScreenAction.OnTagSelected(
                                VaultItemTag(
                                    id = state.tagInput,
                                    title = state.tagInput
                                )
                            )
                        )
                    }
                )
            }
        }

        FlowRow(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            state.tags.forEach { tag ->
                InputChip(
                    selected = false,
                    onClick = { onAction(AddEditPasswordScreenAction.OnTagRemoved(tag)) },
                    label = { Text(tag.title) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove tag"
                        )
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}