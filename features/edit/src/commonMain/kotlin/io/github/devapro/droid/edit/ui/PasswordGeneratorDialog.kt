package io.github.devapro.droid.edit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState
import io.github.devapro.droid.edit.model.PasswordGeneratorOptions

@Composable
fun PasswordGeneratorDialog(
    state: AddEditPasswordScreenState.Success,
    onAction: (AddEditPasswordScreenAction) -> Unit
) {
    val options = state.generatorOptions

    AlertDialog(
        onDismissRequest = { onAction(AddEditPasswordScreenAction.OnDismissGeneratorDialog) },
        title = { Text("Generate password") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.generatorPreview.ifEmpty { "—" },
                                style = MaterialTheme.typography.bodyLarge,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = { onAction(AddEditPasswordScreenAction.OnRegenerateGeneratorPreview) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Regenerate"
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Length: ${options.length}",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Slider(
                    value = options.length.toFloat(),
                    onValueChange = { newValue ->
                        onAction(
                            AddEditPasswordScreenAction.OnGeneratorOptionsChanged(
                                options.copy(length = newValue.toInt())
                            )
                        )
                    },
                    valueRange = PasswordGeneratorOptions.MIN_LENGTH.toFloat()..PasswordGeneratorOptions.MAX_LENGTH.toFloat(),
                    steps = PasswordGeneratorOptions.MAX_LENGTH - PasswordGeneratorOptions.MIN_LENGTH - 1
                )

                GeneratorOption(
                    label = "Uppercase (A–Z)",
                    checked = options.includeUppercase,
                    onCheckedChange = {
                        onAction(
                            AddEditPasswordScreenAction.OnGeneratorOptionsChanged(
                                options.copy(includeUppercase = it)
                            )
                        )
                    }
                )
                GeneratorOption(
                    label = "Lowercase (a–z)",
                    checked = options.includeLowercase,
                    onCheckedChange = {
                        onAction(
                            AddEditPasswordScreenAction.OnGeneratorOptionsChanged(
                                options.copy(includeLowercase = it)
                            )
                        )
                    }
                )
                GeneratorOption(
                    label = "Digits (0–9)",
                    checked = options.includeDigits,
                    onCheckedChange = {
                        onAction(
                            AddEditPasswordScreenAction.OnGeneratorOptionsChanged(
                                options.copy(includeDigits = it)
                            )
                        )
                    }
                )
                GeneratorOption(
                    label = "Symbols (!@#…)",
                    checked = options.includeSymbols,
                    onCheckedChange = {
                        onAction(
                            AddEditPasswordScreenAction.OnGeneratorOptionsChanged(
                                options.copy(includeSymbols = it)
                            )
                        )
                    }
                )
                GeneratorOption(
                    label = "Exclude ambiguous (l, 1, O, 0, …)",
                    checked = options.excludeAmbiguous,
                    onCheckedChange = {
                        onAction(
                            AddEditPasswordScreenAction.OnGeneratorOptionsChanged(
                                options.copy(excludeAmbiguous = it)
                            )
                        )
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onAction(AddEditPasswordScreenAction.OnAcceptGeneratedPassword) },
                enabled = state.generatorPreview.isNotEmpty()
            ) {
                Text("Use password")
            }
        },
        dismissButton = {
            TextButton(onClick = { onAction(AddEditPasswordScreenAction.OnDismissGeneratorDialog) }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun GeneratorOption(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
