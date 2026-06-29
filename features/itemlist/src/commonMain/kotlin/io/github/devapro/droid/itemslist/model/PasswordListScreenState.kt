package io.github.devapro.droid.itemslist.model

import io.github.devapro.droid.data.model.ItemModel
import io.github.devapro.droid.data.vault.VaultItemTag
import io.github.devapro.droid.itemlist.PasswordTagFilterType

sealed interface PasswordListScreenState {
    data object Loading : PasswordListScreenState

    data class Error(val message: String) : PasswordListScreenState

    data class Success(
        val title: String,
        val tagFilterType: PasswordTagFilterType,
        val selectedTag: VaultItemTag?,
        val passwords: List<ItemModel>,
        val filteredPasswords: List<ItemModel>,
        val searchQuery: String,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
        val hasSearchQuery: Boolean,
        val sortOrder: SortOrder = SortOrder.NAME_ASC
    ) : PasswordListScreenState
}

enum class SortOrder(val label: String) {
    NAME_ASC("Name (A–Z)"),
    NAME_DESC("Name (Z–A)"),
    CREATED_DESC("Newest first"),
    CREATED_ASC("Oldest first"),
    UPDATED_DESC("Recently updated"),
    LAST_USED_DESC("Recently used")
} 