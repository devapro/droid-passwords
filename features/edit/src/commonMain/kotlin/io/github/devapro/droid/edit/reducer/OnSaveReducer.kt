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
import kotlin.time.Clock
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
            val now = Clock.System.now().toEpochMilliseconds()
            val existing = currentState.itemId?.let { id ->
                runtimeRepository.getVault().items.firstOrNull { it.id == id }
            }
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
                tags = currentState.tags,
                totpSecret = currentState.totpSecret.trim().ifEmpty { null },
                createdAt = existing?.createdAt ?: now,
                updatedAt = now,
                lastUsedAt = existing?.lastUsedAt
            )

            runtimeRepository.addOrUpdateVault(item)
            // Read descriptor + contents atomically so a concurrent background sync can
            // never pair this vault's descriptor with another vault's items on disk.
            val snapshot = runtimeRepository.getActiveSnapshot()
            val result = if (snapshot == null) {
                AppResult.Failure(Exception("No active vault"))
            } else {
                repository.saveVault(descriptor = snapshot.descriptor, vaultModel = snapshot.vault)
            }
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
