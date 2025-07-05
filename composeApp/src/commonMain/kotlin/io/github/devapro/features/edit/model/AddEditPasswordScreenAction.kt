package io.github.devapro.features.edit.model

import io.github.devapro.model.ItemModel

sealed interface AddEditPasswordScreenAction {
    data class InitScreen(val item: ItemModel?) : AddEditPasswordScreenAction

    data class OnTitleChanged(val title: String) : AddEditPasswordScreenAction

    data class OnUsernameChanged(val username: String) : AddEditPasswordScreenAction

    data class OnPasswordChanged(val password: String) : AddEditPasswordScreenAction

    data class OnUrlChanged(val url: String) : AddEditPasswordScreenAction

    data class OnDescriptionChanged(val description: String) : AddEditPasswordScreenAction

    data object OnTogglePasswordVisibility : AddEditPasswordScreenAction

    data object OnToggleAdditionalFields : AddEditPasswordScreenAction

    data object OnGeneratePassword : AddEditPasswordScreenAction

    data class OnAdditionalFieldNameChanged(val index: Int, val name: String) : AddEditPasswordScreenAction

    data class OnAdditionalFieldValueChanged(val index: Int, val value: String) : AddEditPasswordScreenAction

    data object OnAddAdditionalField : AddEditPasswordScreenAction

    data class OnRemoveAdditionalField(val index: Int) : AddEditPasswordScreenAction

    data object OnSaveClicked : AddEditPasswordScreenAction

    data object OnBackClicked : AddEditPasswordScreenAction

    data object OnDeleteClicked : AddEditPasswordScreenAction
} 