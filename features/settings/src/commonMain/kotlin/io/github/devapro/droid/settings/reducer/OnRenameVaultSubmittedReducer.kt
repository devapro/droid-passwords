package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnRenameVaultSubmittedReducer(
    private val registryRepository: VaultRegistryRepository,
    private val runtimeRepository: VaultRuntimeRepository,
    private val fileRepository: VaultFileRepository,
) : Reducer<SettingsScreenAction.OnRenameVaultSubmitted, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnRenameVaultSubmitted::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnRenameVaultSubmitted,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction.OnRenameVaultSubmitted, SettingsScreenEvent?> {
        val current = getState()
        if (current !is SettingsScreenState.Success) {
            return Reducer.Result(state = current, action = null, event = null)
        }

        val name = current.renameDraft.trim()
        if (name.isBlank()) {
            return Reducer.Result(
                state = current.copy(renameError = "Vault name cannot be empty."),
                action = null,
                event = null,
            )
        }

        val descriptor = try {
            registryRepository.requireActiveDescriptor()
        } catch (e: Exception) {
            return Reducer.Result(
                state = current.copy(renameError = "No active vault."),
                action = null,
                event = null,
            )
        }

        val newDescriptor = descriptor.copy(name = name, updatedAt = nowMillis())
        registryRepository.renameVault(descriptor.id, name)

        val activeVault = runCatching { runtimeRepository.getActiveVault() }.getOrNull()
        if (activeVault != null) {
            val updated = activeVault.copy(name = name, updatedAt = nowMillis())
            runtimeRepository.replaceActiveDescriptor(newDescriptor)
            runtimeRepository.replaceActiveVault(updated)
            when (val save = fileRepository.saveVault(newDescriptor, updated)) {
                is AppResult.Failure -> {
                    return Reducer.Result(
                        state = current.copy(renameError = save.error.message ?: "Could not save rename."),
                        action = null,
                        event = null,
                    )
                }
                is AppResult.Success -> Unit
            }
        }

        return Reducer.Result(
            state = current.copy(
                activeVaultName = name,
                isRenameDialogVisible = false,
                renameDraft = "",
                renameError = null,
            ),
            action = null,
            event = SettingsScreenEvent.ShowSuccess("Renamed vault to \"$name\"."),
        )
    }

    private fun nowMillis(): Long {
        @OptIn(kotlin.time.ExperimentalTime::class)
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }
}
