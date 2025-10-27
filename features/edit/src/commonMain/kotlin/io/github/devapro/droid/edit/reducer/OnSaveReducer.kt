package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultAdditionalFieldModel
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultItemModel
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class OnSaveReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val repository: VaultFileRepository
) : Reducer<AddEditPasswordScreenAction.OnSave, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnSave::class

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnSave,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnSave, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success && currentState.isFormValid) {
            val item = VaultItemModel(
                id = currentState.itemId ?: Uuid.random().toHexDashString(),
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
                        state = currentState.copy(isSaving = false),
                        action = null,
                        event = AddEditPasswordScreenEvent.SaveSuccess
                    )
                }

                is AppResult.Failure -> {
                    return Reducer.Result(
                        state = currentState.copy(isSaving = false),
                        action = null,
                        event = AddEditPasswordScreenEvent.ShowMessage(result.error.message.orEmpty())
                    )
                }
            }
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = AddEditPasswordScreenEvent.ShowMessage("Please fill in all required fields")
            )
        }
    }
} 