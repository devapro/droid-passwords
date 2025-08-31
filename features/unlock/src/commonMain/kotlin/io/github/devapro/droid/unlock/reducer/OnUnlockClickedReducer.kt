package io.github.devapro.droid.unlock.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.unlock.model.UnLockVaultScreenAction
import io.github.devapro.droid.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.droid.unlock.model.UnLockVaultScreenState

class OnUnlockClickedReducer
    : Reducer<UnLockVaultScreenAction.OnUnlockClicked, UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {

    override val actionClass = UnLockVaultScreenAction.OnUnlockClicked::class

    override suspend fun reduce(
        action: UnLockVaultScreenAction.OnUnlockClicked,
        getState: () -> UnLockVaultScreenState
    ): Reducer.Result<UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {
        val currentState = getState()

        return if (currentState is UnLockVaultScreenState.Loaded && currentState.isValid && !currentState.isProcessing) {
            Reducer.Result(
                state = currentState.copy(isProcessing = true),
                action = UnLockVaultScreenAction.UnlockVault(currentState.password),
                event = null
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