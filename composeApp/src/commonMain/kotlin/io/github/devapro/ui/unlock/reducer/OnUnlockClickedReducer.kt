package io.github.devapro.ui.unlock.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.unlock.model.UnLockVaultScreenAction
import io.github.devapro.ui.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.ui.unlock.model.UnLockVaultScreenState

class OnUnlockClickedReducer
    : Reducer<UnLockVaultScreenAction.OnUnlockClicked, UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {

    override val actionClass = UnLockVaultScreenAction.OnUnlockClicked::class

    override suspend fun reduce(
        action: UnLockVaultScreenAction.OnUnlockClicked,
        getState: () -> UnLockVaultScreenState
    ): Reducer.Result<UnLockVaultScreenState, UnLockVaultScreenAction.OnUnlockClicked, UnLockVaultScreenEvent?> {
        val currentState = getState()

        return if (currentState is UnLockVaultScreenState.Success && currentState.isValid && !currentState.isProcessing) {
            Reducer.Result(
                state = currentState.copy(isProcessing = true),
                action = null,
                event = UnLockVaultScreenEvent.UnlockVault(currentState.password)
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }
} 