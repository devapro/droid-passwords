package io.github.devapro.droid.tags

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.tags.factory.TagsScreenInitStateFactory
import io.github.devapro.droid.tags.model.TagsScreenAction
import io.github.devapro.droid.tags.model.TagsScreenEvent
import io.github.devapro.droid.tags.model.TagsScreenState

class TagsScreenActionProcessor(
    reducers: Set<Reducer<TagsScreenAction, TagsScreenState, TagsScreenAction, TagsScreenEvent>>,
    initStateFactory: TagsScreenInitStateFactory,
    coroutineContextProvider: CoroutineContextProvider
) : ActionProcessor<TagsScreenState, TagsScreenAction, TagsScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.io
) 