package io.github.devapro.droid.tags

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.tags.model.TagsScreenAction
import io.github.devapro.droid.tags.model.TagsScreenEvent
import io.github.devapro.droid.tags.model.TagsScreenState

class TagsScreenViewModel(
    actionProcessor: TagsScreenActionProcessor
) : MviViewModel<TagsScreenState, TagsScreenAction, TagsScreenEvent>(
    actionProcessor = actionProcessor
) 