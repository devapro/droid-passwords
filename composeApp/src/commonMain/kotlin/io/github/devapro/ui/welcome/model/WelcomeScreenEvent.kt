package io.github.devapro.ui.welcome.model

sealed interface WelcomeScreenEvent {

    data object OnCreateNewVault : WelcomeScreenEvent

    data object OnOpenExistingVault : WelcomeScreenEvent
}