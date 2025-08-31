package io.github.devapro.droid.tags.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.tags.model.TagsScreenAction
import io.github.devapro.droid.tags.model.TagsScreenEvent
import io.github.devapro.droid.tags.model.TagsScreenState

class OnBackClickedReducer
    : Reducer<TagsScreenAction.OnBackClicked, TagsScreenState, TagsScreenAction, TagsScreenEvent> {

    override val actionClass = TagsScreenAction.OnBackClicked::class

    override suspend fun reduce(
        action: TagsScreenAction.OnBackClicked,
        getState: () -> TagsScreenState
    ): Reducer.Result<TagsScreenState, TagsScreenAction.OnBackClicked, TagsScreenEvent?> {
        val currentState = getState()

        return Reducer.Result(
            state = currentState,
            action = null,
            event = TagsScreenEvent.NavigateBack
        )
    }
} 