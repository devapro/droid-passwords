package io.github.devapro.droid.itemdetails.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenAction
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenEvent
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenState

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