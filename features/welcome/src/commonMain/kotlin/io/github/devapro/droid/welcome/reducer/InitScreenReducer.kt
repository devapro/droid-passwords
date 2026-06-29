package io.github.devapro.droid.welcome.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenEvent
import io.github.devapro.droid.welcome.model.WelcomeScreenState

class InitScreenReducer(
    private val vaultFileRepository: VaultFileRepository,
    private val syncStateStore: SyncStateStore
) : Reducer<WelcomeScreenAction.InitScreen, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.InitScreen::class

    override suspend fun reduce(
        action: WelcomeScreenAction.InitScreen,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent?> {
        val isVaultExists = vaultFileRepository.isVaultExists()
        return Reducer.Result(
            state = WelcomeScreenState.Success(
                isVaultExists = isVaultExists,
                syncServerUrl = syncStateStore.getServerUrl()
            ),
            action = null,
            event = null
        )
    }
}
