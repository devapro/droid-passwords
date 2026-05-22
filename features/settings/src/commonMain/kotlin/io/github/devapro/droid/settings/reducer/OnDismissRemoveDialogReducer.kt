package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnDismissRemoveDialogReducer
    : Reducer<SettingsScreenAction.OnDismissRemoveDialog, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnDismissRemoveDialog::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnDismissRemoveDialog,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction.OnDismissRemoveDialog, SettingsScreenEvent?> {
        val current = getState()
        return if (current is SettingsScreenState.Success) {
            Reducer.Result(
                state = current.copy(
                    isRemoveDialogVisible = false,
                    removeAlsoDeleteFile = false,
                    vaultActionError = null,
                ),
                action = null,
                event = null,
            )
        } else Reducer.Result(state = current, action = null, event = null)
    }
}
