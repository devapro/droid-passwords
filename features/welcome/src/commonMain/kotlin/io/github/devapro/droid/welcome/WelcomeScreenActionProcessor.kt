package io.github.devapro.droid.welcome

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.welcome.factory.WelcomeScreenInitStateFactory
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenEvent
import io.github.devapro.droid.welcome.model.WelcomeScreenState

class WelcomeScreenActionProcessor(
    private val initStateFactory: WelcomeScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<WelcomeScreenAction, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent>>,
) : ActionProcessor<
        WelcomeScreenState,
        WelcomeScreenAction,
        WelcomeScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default
)