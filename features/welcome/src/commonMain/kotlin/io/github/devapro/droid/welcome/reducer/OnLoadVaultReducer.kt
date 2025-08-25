package io.github.devapro.droid.welcome.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenEvent
import io.github.devapro.droid.welcome.model.WelcomeScreenState

class OnLoadVaultReducer
    : Reducer<WelcomeScreenAction.OnLoadVault, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.OnLoadVault::class

    override suspend fun reduce(
        action: WelcomeScreenAction.OnLoadVault,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction.OnLoadVault, WelcomeScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = WelcomeScreenEvent.OnLoadVault
        )
    }
} 