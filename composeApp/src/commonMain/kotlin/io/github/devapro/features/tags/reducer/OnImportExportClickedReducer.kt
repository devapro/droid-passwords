package io.github.devapro.features.tags.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.tags.model.TagsScreenAction
import io.github.devapro.features.tags.model.TagsScreenEvent
import io.github.devapro.features.tags.model.TagsScreenState

class OnImportExportClickedReducer
    :
    Reducer<TagsScreenAction.OnImportExportClicked, TagsScreenState, TagsScreenAction, TagsScreenEvent> {

    override val actionClass = TagsScreenAction.OnImportExportClicked::class

    override suspend fun reduce(
        action: TagsScreenAction.OnImportExportClicked,
        getState: () -> TagsScreenState
    ): Reducer.Result<TagsScreenState, TagsScreenAction.OnImportExportClicked, TagsScreenEvent?> {
        return Reducer.Result(
            state = getState(),
            action = null,
            event = TagsScreenEvent.NavigateToImportExport
        )
    }
} 