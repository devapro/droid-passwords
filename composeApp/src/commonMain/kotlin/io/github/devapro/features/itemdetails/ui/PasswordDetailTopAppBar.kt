package io.github.devapro.features.itemdetails.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordDetailTopAppBar(
    state: PasswordDetailScreenState.Success,
    onAction: (PasswordDetailScreenAction) -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = state.item.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { onAction(PasswordDetailScreenAction.OnBackClicked) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = { onAction(PasswordDetailScreenAction.OnEditClicked) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { onAction(PasswordDetailScreenAction.OnShareClicked) }) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
            IconButton(onClick = { onAction(PasswordDetailScreenAction.OnDeleteClicked) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    )
}