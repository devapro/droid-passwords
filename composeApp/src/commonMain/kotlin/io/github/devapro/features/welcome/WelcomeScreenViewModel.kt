package io.github.devapro.features.welcome

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.features.welcome.model.WelcomeScreenAction
import io.github.devapro.features.welcome.model.WelcomeScreenEvent
import io.github.devapro.features.welcome.model.WelcomeScreenState

class WelcomeScreenViewModel(
    actionProcessor: WelcomeScreenActionProcessor
) : MviViewModel<WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent>(
    actionProcessor
)