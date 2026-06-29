package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncManager
import io.github.devapro.droid.data.sync.SyncScheduler
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnRegisterSubmittedReducer(
    private val syncManager: SyncManager,
    private val syncStateStore: SyncStateStore,
    private val syncScheduler: SyncScheduler
) : Reducer<SettingsScreenAction.OnRegisterSubmitted, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnRegisterSubmitted::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnRegisterSubmitted,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success) {
            return Reducer.Result(currentState)
        }
        if (action.url.isBlank() || action.username.isBlank() || action.password.isBlank()) {
            return Reducer.Result(currentState.copy(authError = "All fields are required"))
        }

        return when (val result = syncManager.register(action.url.trim(), action.username.trim(), action.password)) {
            is AppResult.Success -> {
                syncScheduler.startIfEnabled()
                Reducer.Result(
                    state = currentState.copy(
                        isAuthDialogVisible = false,
                        isAuthInProgress = false,
                        authError = null,
                        isLoggedIn = true,
                        syncServerUrl = action.url.trim(),
                        syncUsername = syncStateStore.getUsername()
                    ),
                    event = SettingsScreenEvent.ShowSuccess("Account created and signed in")
                )
            }
            is AppResult.Failure -> Reducer.Result(
                state = currentState.copy(
                    isAuthInProgress = false,
                    authError = result.error.message ?: "Registration failed"
                )
            )
        }
    }
}
