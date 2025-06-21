package io.github.devapro.ui.welcome

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.ui.welcome.model.WelcomeScreenAction
import io.github.devapro.ui.welcome.model.WelcomeScreenEvent
import io.github.devapro.ui.welcome.model.WelcomeScreenState

class WelcomeScreenViewModel(
    actionProcessor: WelcomeScreenActionProcessor
): MviViewModel<WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent>(
    actionProcessor
)