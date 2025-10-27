package io.github.devapro.droid.itemslist.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import io.github.devapro.droid.core.ui.EOutlinedTextField
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    state: PasswordListScreenState.Success,
    onAction: (PasswordListScreenAction) -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    val searchFieldFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        searchFieldFocusRequester.requestFocus()
    }
    TopAppBar(
        title = {
            EOutlinedTextField(
                value = state.searchQuery,
                onValueChange = {
                    onAction(PasswordListScreenAction.OnSearchChanged(it))
                },
                placeholder = { Text("Search passwords...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(searchFieldFocusRequester)
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                onCancelButtonClicked()
            }) {
                Icon(Icons.Default.Clear, contentDescription = "Close search")
            }
        }
    )
}