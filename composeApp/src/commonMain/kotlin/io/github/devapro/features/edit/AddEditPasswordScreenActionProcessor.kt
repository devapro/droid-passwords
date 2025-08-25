package io.github.devapro.features.edit

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.features.edit.factory.AddEditPasswordScreenInitStateFactory
import io.github.devapro.features.edit.model.AddEditPasswordScreenAction
import io.github.devapro.features.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.features.edit.model.AddEditPasswordScreenState

class AddEditPasswordScreenActionProcessor(
    private val initStateFactory: AddEditPasswordScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<AddEditPasswordScreenAction, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent>>,
) : ActionProcessor<
        AddEditPasswordScreenState,
        AddEditPasswordScreenAction,
        AddEditPasswordScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default
) 