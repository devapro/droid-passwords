package io.github.devapro.ui.welcome.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.welcome.model.WelcomeScreenAction
import io.github.devapro.ui.welcome.model.WelcomeScreenEvent
import io.github.devapro.ui.welcome.model.WelcomeScreenState

class OnCreateNewVaultReducer
    : Reducer<WelcomeScreenAction.OnCreateNewVault, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.OnCreateNewVault::class

    override suspend fun reduce(
        action: WelcomeScreenAction.OnCreateNewVault,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction.OnCreateNewVault, WelcomeScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = action,
            event = WelcomeScreenEvent.OnCreateNewVault
        )
    }
} 