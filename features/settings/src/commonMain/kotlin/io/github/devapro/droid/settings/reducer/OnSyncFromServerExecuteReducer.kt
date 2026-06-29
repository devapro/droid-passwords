package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncManager
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnSyncFromServerExecuteReducer(
    private val syncManager: SyncManager,
    private val syncStateStore: SyncStateStore
) : Reducer<SettingsScreenAction.OnSyncFromServerExecute, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnSyncFromServerExecute::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnSyncFromServerExecute,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success) {
            return Reducer.Result(currentState)
        }
        return when (val result = syncManager.pullFromServer()) {
            is AppResult.Success -> {
                val summary = result.value
                Reducer.Result(
                    state = currentState.copy(
                        isSyncing = false,
                        lastSyncStatus = syncStateStore.getLastStatus(),
                        lastSyncAt = syncStateStore.getLastSyncAt()
                    ),
                    event = SettingsScreenEvent.ShowSuccess(
                        "Downloaded ${summary.pulled} change(s), removed ${summary.deleted}"
                    )
                )
            }
            is AppResult.Failure -> Reducer.Result(
                state = currentState.copy(isSyncing = false),
                event = SettingsScreenEvent.ShowError(result.error.message ?: "Download failed")
            )
        }
    }
}
