package io.github.devapro.ui.itemdetails.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenState

class InitScreenReducer
    : Reducer<PasswordDetailScreenAction.InitScreen, PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent> {

    override val actionClass = PasswordDetailScreenAction.InitScreen::class

    override suspend fun reduce(
        action: PasswordDetailScreenAction.InitScreen,
        getState: () -> PasswordDetailScreenState
    ): Reducer.Result<PasswordDetailScreenState, PasswordDetailScreenAction.InitScreen, PasswordDetailScreenEvent?> {
        return Reducer.Result(
            state = PasswordDetailScreenState.Success(
                item = action.item,
                isPasswordVisible = false,
                isLoading = false,
                isFavorite = false // TODO: Get from repository
            ),
            action = null,
            event = null
        )
    }
} 