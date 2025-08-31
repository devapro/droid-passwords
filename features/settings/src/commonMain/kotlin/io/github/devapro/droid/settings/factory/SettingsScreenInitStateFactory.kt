package io.github.devapro.droid.settings.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.droid.settings.model.SettingsScreenState

/**
 * Factory for creating the initial state of the settings screen.
 * The screen starts in a loading state while settings are being loaded.
 */
class SettingsScreenInitStateFactory : InitStateFactory<SettingsScreenState> {

    override fun createInitState(): SettingsScreenState {
        return SettingsScreenState.Loading
    }
}