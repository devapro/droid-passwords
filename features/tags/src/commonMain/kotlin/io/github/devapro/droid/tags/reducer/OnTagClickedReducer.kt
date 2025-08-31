package io.github.devapro.droid.tags.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.tags.model.TagsScreenAction
import io.github.devapro.droid.tags.model.TagsScreenEvent
import io.github.devapro.droid.tags.model.TagsScreenState

class OnTagClickedReducer
    : Reducer<TagsScreenAction.OnTagClicked, TagsScreenState, TagsScreenAction, TagsScreenEvent> {

    override val actionClass = TagsScreenAction.OnTagClicked::class

    override suspend fun reduce(
        action: TagsScreenAction.OnTagClicked,
        getState: () -> TagsScreenState
    ): Reducer.Result<TagsScreenState, TagsScreenAction.OnTagClicked, TagsScreenEvent?> {
        val currentState = getState()

        return if (currentState is TagsScreenState.Success) {
            Reducer.Result(
                state = currentState,
                action = null,
                event = TagsScreenEvent.NavigateToTagDetail(action.tag)
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