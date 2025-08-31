package io.github.devapro.droid.tags.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.tags.model.TagsScreenAction
import io.github.devapro.droid.tags.model.TagsScreenEvent
import io.github.devapro.droid.tags.model.TagsScreenState

class OnAddPasswordClickedReducer
    :
    Reducer<TagsScreenAction.OnAddPasswordClicked, TagsScreenState, TagsScreenAction, TagsScreenEvent> {

    override val actionClass = TagsScreenAction.OnAddPasswordClicked::class

    override suspend fun reduce(
        action: TagsScreenAction.OnAddPasswordClicked,
        getState: () -> TagsScreenState
    ): Reducer.Result<TagsScreenState, TagsScreenAction.OnAddPasswordClicked, TagsScreenEvent?> {
        val currentState = getState()

        return Reducer.Result(
            state = currentState,
            action = null,
            event = TagsScreenEvent.NavigateToAddPassword
        )
    }
} 