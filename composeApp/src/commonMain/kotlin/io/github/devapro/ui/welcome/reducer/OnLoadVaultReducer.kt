package io.github.devapro.ui.welcome.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.data.vault.VaultFileRepository
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.devapro.ui.welcome.model.WelcomeScreenAction
import io.github.devapro.ui.welcome.model.WelcomeScreenEvent
import io.github.devapro.ui.welcome.model.WelcomeScreenState

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