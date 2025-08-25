package io.github.devapro.features.itemdetails.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.data.vault.VaultItemTag

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsSection(tags: List<VaultItemTag>) {
    if (tags.isNotEmpty()) {
        FlowRow(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            tags.forEach { tag ->
                InputChip(
                    selected = false,
                    onClick = { /* No action */ },
                    label = { Text(tag.title) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}