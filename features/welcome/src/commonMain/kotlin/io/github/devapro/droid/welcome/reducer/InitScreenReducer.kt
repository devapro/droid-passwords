package io.github.devapro.droid.welcome.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenEvent
import io.github.devapro.droid.welcome.model.WelcomeScreenState

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