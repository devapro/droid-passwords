package io.github.devapro.droid.vaultlist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenEvent
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

class OnBackClickedReducer
    : Reducer<VaultListScreenAction.OnBackClicked, VaultListScreenState, VaultListScreenAction, VaultListScreenEvent> {

    override val actionClass = VaultListScreenAction.OnBackClicked::class

    override suspend fun reduce(
        action: VaultListScreenAction.OnBackClicked,
        getState: () -> VaultListScreenState
    ): Reducer.Result<VaultListScreenState, VaultListScreenAction.OnBackClicked, VaultListScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = VaultListScreenEvent.NavigateBack,
        )
    }
}
