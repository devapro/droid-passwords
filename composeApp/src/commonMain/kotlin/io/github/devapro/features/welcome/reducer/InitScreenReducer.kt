package io.github.devapro.features.welcome.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.data.vault.VaultFileRepository
import io.github.devapro.features.welcome.model.WelcomeScreenAction
import io.github.devapro.features.welcome.model.WelcomeScreenEvent
import io.github.devapro.features.welcome.model.WelcomeScreenState

class InitScreenReducer(
    private val vaultFileRepository: VaultFileRepository
): Reducer<WelcomeScreenAction.InitScreen, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.InitScreen::class

    override suspend fun reduce(
        action: WelcomeScreenAction.InitScreen,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction.InitScreen, WelcomeScreenEvent?> {
        val isVaultExists = vaultFileRepository.isVaultExists()
        return Reducer.Result(
            state = WelcomeScreenState.Success(
                isVaultExists = isVaultExists
            ),
            action = null,
            event = null
        )
    }
}