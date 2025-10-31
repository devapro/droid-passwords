package io.github.devapro.droid.itemslist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenState

@Composable
internal fun ItemsList(
    state: PasswordListScreenState.Success,
    onAction: (PasswordListScreenAction) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = state.filteredPasswords,
            key = { it.id }
        ) { password ->
            PasswordItem(
                password = password,
                onItemClick = {
                    onAction(
                        PasswordListScreenAction.OnPasswordItemClicked(
                            password
                        )
                    )
                }
            )
        }
    }
}