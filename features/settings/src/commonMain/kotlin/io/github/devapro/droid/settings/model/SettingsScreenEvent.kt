package io.github.devapro.droid.settings.model

sealed interface SettingsScreenEvent {
    data object NavigateBack : SettingsScreenEvent

    data class ShowError(val message: String) : SettingsScreenEvent

    data class ShowSuccess(val message: String) : SettingsScreenEvent

    data object ShowFilePathPicker : SettingsScreenEvent
}