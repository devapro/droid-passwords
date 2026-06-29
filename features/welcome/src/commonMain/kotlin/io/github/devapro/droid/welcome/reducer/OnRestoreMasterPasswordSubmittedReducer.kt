package io.github.devapro.droid.welcome.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncManager
import io.github.devapro.droid.data.sync.SyncScheduler
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenEvent
import io.github.devapro.droid.welcome.model.WelcomeScreenState

class OnRestoreMasterPasswordSubmittedReducer(
    private val syncManager: SyncManager,
    private val syncScheduler: SyncScheduler
) : Reducer<WelcomeScreenAction.OnRestoreMasterPasswordSubmitted, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.OnRestoreMasterPasswordSubmitted::class

    override suspend fun reduce(
        action: WelcomeScreenAction.OnRestoreMasterPasswordSubmitted,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent?> {
        val currentState = getState()
        if (currentState !is WelcomeScreenState.Success) {
            return Reducer.Result(currentState)
        }
        if (action.masterPassword.isBlank()) {
            return Reducer.Result(currentState.copy(restoreError = "Master password cannot be empty"))
        }

        val loadingState = currentState.copy(isRestoreInProgress = true, restoreError = null)

        return when (val result = syncManager.restoreVaultFromServer(action.masterPassword)) {
            is AppResult.Success -> {
                syncScheduler.startIfEnabled()
                val summary = result.value
                val message = if (summary.pulled == 0) {
                    "Vault created. No passwords were found on the sync server yet."
                } else {
                    "Restored ${summary.pulled} password(s) from the sync server."
                }
                Reducer.Result(
                    state = loadingState.copy(
                        isMasterPasswordDialogVisible = false,
                        isRestoreInProgress = false,
                        restoreError = null,
                        isVaultExists = true
                    ),
                    event = WelcomeScreenEvent.OnRestoreSuccess(message)
                )
            }
            is AppResult.Failure -> Reducer.Result(
                state = loadingState.copy(
                    isRestoreInProgress = false,
                    restoreError = result.error.message ?: "Restore failed"
                )
            )
        }
    }
}
