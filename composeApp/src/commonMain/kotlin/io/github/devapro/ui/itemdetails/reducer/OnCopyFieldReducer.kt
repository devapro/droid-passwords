package io.github.devapro.ui.itemdetails.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.ui.itemdetails.model.PasswordDetailScreenState

class OnCopyFieldReducer
    : Reducer<PasswordDetailScreenAction.OnCopyField, PasswordDetailScreenState, PasswordDetailScreenAction, PasswordDetailScreenEvent> {

    override val actionClass = PasswordDetailScreenAction.OnCopyField::class

    override suspend fun reduce(
        action: PasswordDetailScreenAction.OnCopyField,
        getState: () -> PasswordDetailScreenState
    ): Reducer.Result<PasswordDetailScreenState, PasswordDetailScreenAction.OnCopyField, PasswordDetailScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = PasswordDetailScreenEvent.CopyToClipboard(action.fieldName, action.value)
        )
    }
} 