package io.github.devapro.features.welcome

import io.github.devapro.core.mvi.ActionProcessor
import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.welcome.factory.WelcomeScreenInitStateFactory
import io.github.devapro.features.welcome.model.WelcomeScreenAction
import io.github.devapro.features.welcome.model.WelcomeScreenEvent
import io.github.devapro.features.welcome.model.WelcomeScreenState

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