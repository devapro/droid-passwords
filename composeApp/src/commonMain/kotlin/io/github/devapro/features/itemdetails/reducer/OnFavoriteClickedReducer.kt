package io.github.devapro.features.itemdetails.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenState

class OnFavoriteClickedReducer
    : Reducer<PasswordDetailScreenAction.OnFavoriteClicked, PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent> {

    override val actionClass = PasswordDetailScreenAction.OnFavoriteClicked::class

    override suspend fun reduce(
        action: PasswordDetailScreenAction.OnFavoriteClicked,
        getState: () -> PasswordDetailScreenState
    ): Reducer.Result<PasswordDetailScreenState, PasswordDetailScreenAction.OnFavoriteClicked, PasswordDetailScreenEvent?> {
        val currentState = getState()

        return if (currentState is PasswordDetailScreenState.Success) {
            val newFavoriteState = !currentState.isFavorite
            Reducer.Result(
                state = currentState.copy(isFavorite = newFavoriteState),
                action = null,
                event = PasswordDetailScreenEvent.UpdateFavorite(currentState.item, newFavoriteState)
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