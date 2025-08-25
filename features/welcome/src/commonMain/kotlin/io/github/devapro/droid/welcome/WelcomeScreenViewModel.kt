package io.github.devapro.droid.welcome

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenEvent
import io.github.devapro.droid.welcome.model.WelcomeScreenState

class WelcomeScreenViewModel(
    actionProcessor: WelcomeScreenActionProcessor
) : MviViewModel<WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent>(
    actionProcessor
)