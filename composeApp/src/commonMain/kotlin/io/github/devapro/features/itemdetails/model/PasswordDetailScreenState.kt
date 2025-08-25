package io.github.devapro.features.itemdetails.model

import io.github.devapro.droid.data.model.ItemModel

sealed interface PasswordDetailScreenState {
    data object Loading : PasswordDetailScreenState

    data class Error(val message: String) : PasswordDetailScreenState

    data class Success(
        val item: ItemModel,
        val isPasswordVisible: Boolean,
        val isLoading: Boolean,
        val showDeleteConfirmation: Boolean
    ) : PasswordDetailScreenState
} 