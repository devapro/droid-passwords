package io.github.devapro.features.welcome.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.welcome.model.WelcomeScreenAction
import io.github.devapro.features.welcome.model.WelcomeScreenEvent
import io.github.devapro.features.welcome.model.WelcomeScreenState

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