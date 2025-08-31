package io.github.devapro.droid.setlock

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.setlock.factory.SetLockPasswordScreenInitStateFactory
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.droid.setlock.model.SetLockPasswordScreenState

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