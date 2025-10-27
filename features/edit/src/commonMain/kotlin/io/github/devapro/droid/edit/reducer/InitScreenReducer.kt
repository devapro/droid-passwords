package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class InitScreenReducer(
    private val vaultRuntimeRepository: VaultRuntimeRepository
) :
    Reducer<AddEditPasswordScreenAction.InitScreen, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.InitScreen::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.InitScreen,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent?> {
        val item = action.item
        val allTags = vaultRuntimeRepository.getAllTags()
        return Reducer.Result(
            state = AddEditPasswordScreenState.Success(
                isEditMode = item != null,
                itemId = item?.id,
                title = item?.title ?: "",
                username = item?.username ?: "",
                password = item?.password ?: "",
                url = item?.url ?: "",
                description = item?.description ?: "",
                additionalFields = item?.additionalFields ?: emptyList(),
                tags = item?.tags ?: action.selectedTag?.let { listOf(it) } ?: emptyList(),
                tagInput = "",
                allTags = allTags,
                isPasswordVisible = false,
                isAdditionalFieldsVisible = false,
                isSaving = false,
                titleError = null,
                passwordError = null,
                isFormValid = validateForm(item?.title ?: "", item?.password ?: "")
            )
        )
    }

    private fun validateForm(title: String, password: String): Boolean {
        return title.isNotBlank() && password.isNotBlank()
    }
} 