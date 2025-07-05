package io.github.devapro.features.itemslist.model

import io.github.devapro.model.ItemModel

sealed interface PasswordListScreenState {
    data object Loading : PasswordListScreenState

    data class Error(val message: String) : PasswordListScreenState

    data class Success(
        val passwords: List<ItemModel>,
        val filteredPasswords: List<ItemModel>,
        val searchQuery: String,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
        val isEmpty: Boolean,
        val hasSearchQuery: Boolean
    ) : PasswordListScreenState
} 