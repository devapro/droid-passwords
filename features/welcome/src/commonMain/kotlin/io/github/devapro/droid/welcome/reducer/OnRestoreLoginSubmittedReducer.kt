package io.github.devapro.droid.welcome.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncManager
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenEvent
import io.github.devapro.droid.welcome.model.WelcomeScreenState

class OnRestoreLoginSubmittedReducer(
    private val syncManager: SyncManager
) : Reducer<WelcomeScreenAction.OnRestoreLoginSubmitted, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.OnRestoreLoginSubmitted::class

    override suspend fun reduce(
        action: WelcomeScreenAction.OnRestoreLoginSubmitted,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent?> {
        val currentState = getState()
        if (currentState !is WelcomeScreenState.Success) {
            return Reducer.Result(currentState)
        }
        if (action.url.isBlank() || action.username.isBlank() || action.password.isBlank()) {
            return Reducer.Result(currentState.copy(restoreError = "All fields are required"))
        }

        val loadingState = currentState.copy(isRestoreInProgress = true, restoreError = null)

        return when (val result = syncManager.login(action.url.trim(), action.username.trim(), action.password)) {
            is AppResult.Success -> Reducer.Result(
                state = loadingState.copy(
                    isRestoreAuthDialogVisible = false,
                    isMasterPasswordDialogVisible = true,
                    isRestoreInProgress = false,
                    restoreError = null,
                    syncServerUrl = action.url.trim()
                )
            )
            is AppResult.Failure -> Reducer.Result(
                state = loadingState.copy(
                    isRestoreInProgress = false,
                    restoreError = result.error.message ?: "Login failed"
                )
            )
        }
    }
}
