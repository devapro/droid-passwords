package io.github.devapro.features.unlock.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.unlock.model.UnLockVaultScreenAction
import io.github.devapro.features.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.features.unlock.model.UnLockVaultScreenState

class OnBackClickedReducer
    : Reducer<UnLockVaultScreenAction.OnBackClicked, UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {

    override val actionClass = UnLockVaultScreenAction.OnBackClicked::class

    override suspend fun reduce(
        action: UnLockVaultScreenAction.OnBackClicked,
        getState: () -> UnLockVaultScreenState
    ): Reducer.Result<UnLockVaultScreenState, UnLockVaultScreenAction.OnBackClicked, UnLockVaultScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = UnLockVaultScreenEvent.NavigateBack
        )
    }
} 