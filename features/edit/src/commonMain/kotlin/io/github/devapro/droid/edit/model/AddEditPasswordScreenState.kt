package io.github.devapro.droid.edit.model

import io.github.devapro.droid.data.model.AdditionalFieldsModel
import io.github.devapro.droid.data.vault.VaultItemTag

sealed interface AddEditPasswordScreenState {
    data object Loading : AddEditPasswordScreenState

    data class Error(val message: String) : AddEditPasswordScreenState

    data class Success(
        val isEditMode: Boolean,
        val itemId: String?,
        val title: String,
        val username: String,
        val password: String,
        val url: String,
        val description: String,
        val additionalFields: List<AdditionalFieldsModel>,
        val tags: List<VaultItemTag>,
        val tagInput: String,
        val allTags: List<VaultItemTag>,
        val isPasswordVisible: Boolean,
        val isAdditionalFieldsVisible: Boolean,
        val isSaving: Boolean,
        val titleError: String?,
        val passwordError: String?,
        val isFormValid: Boolean,
        val showDeleteConfirmation: Boolean = false
    ) : AddEditPasswordScreenState
} 