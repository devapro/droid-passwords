package io.github.devapro.ui.unlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.unlock.model.UnLockVaultScreenAction
import io.github.devapro.ui.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.ui.unlock.model.UnLockVaultScreenState

class InitScreenReducer
    : Reducer<UnLockVaultScreenAction.InitScreen, UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {

    override val actionClass = UnLockVaultScreenAction.InitScreen::class

    override suspend fun reduce(
        action: UnLockVaultScreenAction.InitScreen,
        getState: () -> UnLockVaultScreenState
    ): Reducer.Result<UnLockVaultScreenState, UnLockVaultScreenAction.InitScreen, UnLockVaultScreenEvent?> {
        return Reducer.Result(
            state = UnLockVaultScreenState.Success(
                password = "",
                isPasswordVisible = false,
                isProcessing = false,
                passwordError = null,
                isValid = false
            ),
            action = null,
            event = null
        )
    }
} 