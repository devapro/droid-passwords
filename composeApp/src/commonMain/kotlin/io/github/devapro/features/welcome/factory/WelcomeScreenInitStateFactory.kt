package io.github.devapro.features.welcome.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.features.welcome.model.WelcomeScreenState

class WelcomeScreenInitStateFactory: InitStateFactory<WelcomeScreenState> {
    override fun createInitState(): WelcomeScreenState {
        return WelcomeScreenState.Loading
    }
}