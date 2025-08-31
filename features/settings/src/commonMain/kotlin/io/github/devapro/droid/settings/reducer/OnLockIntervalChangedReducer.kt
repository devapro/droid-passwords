package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.LockManager
import io.github.devapro.droid.data.SettingsRepository
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * Reducer for handling lock interval changes.
 * Updates the lock interval setting and configures the LockManager accordingly.
 */
class OnLockIntervalChangedReducer(
    private val settingsRepository: SettingsRepository
) : Reducer<SettingsScreenAction.OnLockIntervalChanged, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnLockIntervalChanged::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnLockIntervalChanged,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()

        return if (currentState is SettingsScreenState.Success) {
            try {
                // Save the new lock interval setting
                settingsRepository.setLockInterval(action.interval)

                // Update the LockManager with the new interval
                LockManager.setLockInterval(action.interval)

                Reducer.Result(
                    state = currentState.copy(lockInterval = action.interval),
                    action = null,
                    event = null
                )
            } catch (e: Exception) {
                Reducer.Result(
                    state = currentState,
                    action = null,
                    event = SettingsScreenEvent.ShowError("Failed to update lock interval: ${e.message}")
                )
            }
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }
}