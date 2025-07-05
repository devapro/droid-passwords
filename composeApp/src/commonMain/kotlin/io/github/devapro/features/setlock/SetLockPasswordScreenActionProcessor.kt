package io.github.devapro.features.setlock

import io.github.devapro.core.mvi.ActionProcessor
import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.setlock.factory.SetLockPasswordScreenInitStateFactory
import io.github.devapro.features.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.features.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.features.setlock.model.SetLockPasswordScreenState

class SetLockPasswordScreenActionProcessor(
    private val initStateFactory: SetLockPasswordScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<SetLockPasswordScreenAction, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent>>,
) : ActionProcessor<
        SetLockPasswordScreenState,
        SetLockPasswordScreenAction,
        SetLockPasswordScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default
)