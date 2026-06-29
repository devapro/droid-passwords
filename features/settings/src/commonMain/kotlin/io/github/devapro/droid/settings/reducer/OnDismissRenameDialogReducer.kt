package io.github.devapro.droid.settings.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.settings.model.SettingsScreenAction
import io.github.devapro.droid.settings.model.SettingsScreenEvent
import io.github.devapro.droid.settings.model.SettingsScreenState

class OnDismissRenameDialogReducer
    : Reducer<SettingsScreenAction.OnDismissRenameDialog, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnDismissRenameDialog::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnDismissRenameDialog,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction.OnDismissRenameDialog, SettingsScreenEvent?> {
        val current = getState()
        return if (current is SettingsScreenState.Success) {
            Reducer.Result(
                state = current.copy(
                    isRenameDialogVisible = false,
                    renameDraft = "",
                    renameError = null,
                ),
                action = null,
                event = null,
            )
        } else Reducer.Result(state = current, action = null, event = null)
    }
}
