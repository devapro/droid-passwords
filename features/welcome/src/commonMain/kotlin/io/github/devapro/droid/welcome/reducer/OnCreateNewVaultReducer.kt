package io.github.devapro.droid.welcome.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenEvent
import io.github.devapro.droid.welcome.model.WelcomeScreenState

class OnCreateNewVaultReducer
    : Reducer<WelcomeScreenAction.OnCreateNewVault, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.OnCreateNewVault::class

    override suspend fun reduce(
        action: WelcomeScreenAction.OnCreateNewVault,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction.OnCreateNewVault, WelcomeScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = WelcomeScreenEvent.OnCreateNewVault
        )
    }
} 