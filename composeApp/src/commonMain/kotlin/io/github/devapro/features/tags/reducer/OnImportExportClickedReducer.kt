package io.github.devapro.features.tags.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.tags.model.TagsScreenAction
import io.github.devapro.features.tags.model.TagsScreenEvent
import io.github.devapro.features.tags.model.TagsScreenState

class OnImportExportClickedReducer
    :
    Reducer<TagsScreenAction.OnExportClicked, TagsScreenState, TagsScreenAction, TagsScreenEvent> {

    override val actionClass = TagsScreenAction.OnExportClicked::class

    override suspend fun reduce(
        action: TagsScreenAction.OnExportClicked,
        getState: () -> TagsScreenState
    ): Reducer.Result<TagsScreenState, TagsScreenAction.OnExportClicked, TagsScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = TagsScreenEvent.NavigateToExport
        )
    }
} 