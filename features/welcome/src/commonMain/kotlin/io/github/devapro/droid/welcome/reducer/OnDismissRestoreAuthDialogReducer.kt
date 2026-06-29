package io.github.devapro.droid.welcome.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenEvent
import io.github.devapro.droid.welcome.model.WelcomeScreenState

class OnDismissRestoreAuthDialogReducer :
    Reducer<WelcomeScreenAction.OnDismissRestoreAuthDialog, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.OnDismissRestoreAuthDialog::class

    override suspend fun reduce(
        action: WelcomeScreenAction.OnDismissRestoreAuthDialog,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent?> {
        val currentState = getState()
        return if (currentState is WelcomeScreenState.Success) {
            Reducer.Result(currentState.copy(isRestoreAuthDialogVisible = false, restoreError = null))
        } else {
            Reducer.Result(currentState)
        }
    }
}
