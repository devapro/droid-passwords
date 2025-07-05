package io.github.devapro.features.edit.reducer

import io.github.devapro.core.mvi.AppResult
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.data.vault.VaultAdditionalFieldModel
import io.github.devapro.data.vault.VaultFileRepository
import io.github.devapro.data.vault.VaultItemModel
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.features.edit.model.AddEditPasswordScreenState
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import kotlin.time.ExperimentalTime

class OnSaveClickedReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val repository: VaultFileRepository
)
    : Reducer<AddEditPasswordScreenAction.OnSaveClicked, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnSaveClicked::class

    @OptIn(ExperimentalTime::class)
    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnSaveClicked,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnSaveClicked, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success && currentState.isFormValid) {
            val item = VaultItemModel(
                id = currentState.itemId ?: UUID.generateUUID().toString(),
                title = currentState.title,
                username = currentState.username,
                password = currentState.password,
                url = currentState.url,
                description = currentState.description,
                additionalFields = currentState.additionalFields.map {
                    VaultAdditionalFieldModel(
                        name = it.name,
                        value = it.value
                    )
                },
                tags = currentState.tags
            )

            runtimeRepository.addOrUpdateVault(item)
            val result = repository.saveVault(runtimeRepository.getVault())
            when (result) {
                is AppResult.Success -> {
                    Reducer.Result(
                        state = currentState.copy(isSaving = true),
                        action = null,
                        event = AddEditPasswordScreenEvent.SaveSuccess
                    )
                }

                is AppResult.Failure -> {
                    return Reducer.Result(
                        state = currentState.copy(isSaving = false),
                        action = null,
                        event = AddEditPasswordScreenEvent.SaveError(result.error.message.orEmpty())
                    )
                }
            }
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = AddEditPasswordScreenEvent.SaveError("Please fill in all required fields")
            )
        }
    }
} 