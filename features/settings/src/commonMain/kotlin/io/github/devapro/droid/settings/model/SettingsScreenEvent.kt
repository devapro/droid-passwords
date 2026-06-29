package io.github.devapro.droid.settings.model

sealed interface SettingsScreenEvent {
    data object NavigateBack : SettingsScreenEvent

    data object NavigateAfterVaultRemoved : SettingsScreenEvent

    data class ShowError(val message: String) : SettingsScreenEvent

    data class ShowSuccess(val message: String) : SettingsScreenEvent
}
