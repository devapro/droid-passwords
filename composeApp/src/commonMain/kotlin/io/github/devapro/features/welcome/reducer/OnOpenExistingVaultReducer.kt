package io.github.devapro.features.welcome.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.welcome.model.WelcomeScreenAction
import io.github.devapro.features.welcome.model.WelcomeScreenEvent
import io.github.devapro.features.welcome.model.WelcomeScreenState

class OnOpenExistingVaultReducer
    : Reducer<WelcomeScreenAction.OnOpenExistingVault, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.OnOpenExistingVault::class

    override suspend fun reduce(
        action: WelcomeScreenAction.OnOpenExistingVault,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction.OnOpenExistingVault, WelcomeScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = WelcomeScreenEvent.OnOpenExistingVault
        )
    }
} 