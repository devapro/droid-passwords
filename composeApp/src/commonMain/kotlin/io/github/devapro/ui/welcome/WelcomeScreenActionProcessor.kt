package io.github.devapro.ui.welcome

import io.github.devapro.core.mvi.ActionProcessor
import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.welcome.factory.WelcomeScreenInitStateFactory
import io.github.devapro.ui.welcome.model.WelcomeScreenAction
import io.github.devapro.ui.welcome.model.WelcomeScreenEvent
import io.github.devapro.ui.welcome.model.WelcomeScreenState

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