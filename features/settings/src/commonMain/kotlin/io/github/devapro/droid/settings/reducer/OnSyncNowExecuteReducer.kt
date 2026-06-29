package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncManager
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnSyncNowExecuteReducer(
    private val syncManager: SyncManager,
    private val syncStateStore: SyncStateStore
) : Reducer<SettingsScreenAction.OnSyncNowExecute, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnSyncNowExecute::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnSyncNowExecute,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()
        if (currentState !is SettingsScreenState.Success) {
            return Reducer.Result(currentState)
        }
        return when (val result = syncManager.syncNow()) {
            is AppResult.Success -> {
                val summary = result.value
                Reducer.Result(
                    state = currentState.copy(
                        isSyncing = false,
                        lastSyncStatus = syncStateStore.getLastStatus(),
                        lastSyncAt = syncStateStore.getLastSyncAt()
                    ),
                    event = SettingsScreenEvent.ShowSuccess(
                        if (summary.isEmpty) "Already up to date"
                        else "Synced: ${summary.pushed} up, ${summary.pulled} down, ${summary.deleted} removed"
                    )
                )
            }
            is AppResult.Failure -> Reducer.Result(
                state = currentState.copy(isSyncing = false),
                event = SettingsScreenEvent.ShowError(result.error.message ?: "Sync failed")
            )
        }
    }
}
