package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncManager
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnLinkMasterPasswordExecuteReducer(
    private val syncManager: SyncManager
) : Reducer<SettingsScreenAction.OnLinkMasterPasswordExecute, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnLinkMasterPasswordExecute::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnLinkMasterPasswordExecute,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success) {
            return Reducer.Result(currentState)
        }

        return when (val result = syncManager.restoreServerVaults(action.masterPassword)) {
            is AppResult.Success -> {
                val pending = result.value.pendingVaultCount
                val pulled = result.value.summary.pulled
                if (pending > 0) {
                    // Some vaults still need a different master password (or hit a
                    // transient error) — keep the prompt open so the user can retry.
                    Reducer.Result(
                        state = currentState.copy(
                            isLinkInProgress = false,
                            linkMasterPasswordError = "Couldn't add $pending vault(s) yet. " +
                                "Check the master password and your connection, then try again."
                        )
                    )
                } else {
                    Reducer.Result(
                        state = currentState.copy(
                            isLinkMasterPasswordDialogVisible = false,
                            isLinkInProgress = false,
                            linkMasterPasswordError = null
                        ),
                        event = SettingsScreenEvent.ShowSuccess(
                            if (pulled > 0) "Added vault — pulled $pulled password(s). Find it in Switch Vault."
                            else "Added vault. Find it in Switch Vault."
                        )
                    )
                }
            }
            is AppResult.Failure -> Reducer.Result(
                state = currentState.copy(
                    isLinkInProgress = false,
                    linkMasterPasswordError = result.error.message ?: "Could not add server vault"
                )
            )
        }
    }
}
