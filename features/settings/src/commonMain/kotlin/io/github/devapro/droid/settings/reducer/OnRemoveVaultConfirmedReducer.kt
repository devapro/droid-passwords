package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnRemoveVaultConfirmedReducer(
    private val registryRepository: VaultRegistryRepository,
    private val runtimeRepository: VaultRuntimeRepository,
    private val fileRepository: VaultFileRepository,
) : Reducer<SettingsScreenAction.OnRemoveVaultConfirmed, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnRemoveVaultConfirmed::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnRemoveVaultConfirmed,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction.OnRemoveVaultConfirmed, SettingsScreenEvent?> {
        val current = getState()
        if (current !is SettingsScreenState.Success) {
            return Reducer.Result(state = current, action = null, event = null)
        }

        val descriptor = try {
            registryRepository.requireActiveDescriptor()
        } catch (e: Exception) {
            return Reducer.Result(
                state = current.copy(vaultActionError = "No active vault to remove."),
                action = null,
                event = null,
            )
        }

        runtimeRepository.unloadVault(descriptor.id)
        registryRepository.removeVault(descriptor.id)

        if (current.removeAlsoDeleteFile) {
            when (val result = fileRepository.deleteVaultFile(descriptor)) {
                is AppResult.Failure -> {
                    return Reducer.Result(
                        state = current.copy(
                            isRemoveDialogVisible = false,
                            isPerformingVaultAction = false,
                            vaultActionError = "Removed from registry, but file deletion failed: ${result.error.message ?: ""}",
                        ),
                        action = null,
                        event = SettingsScreenEvent.NavigateAfterVaultRemoved,
                    )
                }
                is AppResult.Success -> Unit
            }
        }

        return Reducer.Result(
            state = current.copy(
                isRemoveDialogVisible = false,
                removeAlsoDeleteFile = false,
                isPerformingVaultAction = false,
                vaultActionError = null,
            ),
            action = null,
            event = SettingsScreenEvent.NavigateAfterVaultRemoved,
        )
    }
}
