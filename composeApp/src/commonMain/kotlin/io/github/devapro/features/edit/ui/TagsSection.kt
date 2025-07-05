package io.github.devapro.features.edit.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devapro.core.ui.EOutlinedTextField
import io.github.devapro.data.vault.VaultItemTag
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsSection(
    state: AddEditPasswordScreenState.Success,
    onAction: (AddEditPasswordScreenAction) -> Unit
) {
    val suggestedTags = state.allTags.filter {
        it.title.contains(state.tagInput, ignoreCase = true) && !state.tags.contains(it)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        EOutlinedTextField(
            value = state.tagInput,
            onValueChange = { onAction(AddEditPasswordScreenAction.OnTagInputChanged(it)) },
            label = { Text("Tags") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = suggestedTags.isNotEmpty() && state.tagInput.isNotBlank(),
            onDismissRequest = { },
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