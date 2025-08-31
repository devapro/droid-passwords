package io.github.devapro.droid.itemdetails.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenState

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
                showDeleteConfirmation = false
            ),
            action = null,
            event = null
        )
    }
} 