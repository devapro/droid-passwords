package io.github.devapro.droid.edit.model

import io.github.devapro.droid.data.model.AdditionalFieldsModel
import io.github.devapro.droid.data.vault.VaultItemTag

sealed interface AddEditPasswordScreenState {
    data object Loading : AddEditPasswordScreenState

    data class Error(val message: String) : AddEditPasswordScreenState

    data class Success(
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
        val totpSecret: String,
        val isPasswordVisible: Boolean,
        val isUrlVisible: Boolean,
        val isDescriptionVisible: Boolean,
        val isTotpVisible: Boolean,
        val isAdditionalFieldsVisible: Boolean,
        val isSaving: Boolean,
        val titleError: String?,
        val passwordError: String?,
        val isFormValid: Boolean,
        val showDeleteConfirmation: Boolean = false,
        val showGeneratorDialog: Boolean = false,
        val generatorOptions: PasswordGeneratorOptions = PasswordGeneratorOptions(),
        val generatorPreview: String = ""
    ) : AddEditPasswordScreenState {
        val isEditMode: Boolean get() = itemId != null
    }
}
