package io.github.devapro.ui.welcome.factory

import io.github.devapro.core.mvi.InitStateFactory
import io.github.devapro.ui.welcome.model.WelcomeScreenState

class WelcomeScreenInitStateFactory: InitStateFactory<WelcomeScreenState> {
    override fun createInitState(): WelcomeScreenState {
        return WelcomeScreenState.Loading
    }
}