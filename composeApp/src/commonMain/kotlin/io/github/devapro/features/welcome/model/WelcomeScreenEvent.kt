package io.github.devapro.features.welcome.model

sealed interface WelcomeScreenEvent {

    data object OnCreateNewVault : WelcomeScreenEvent

    data object OnOpenExistingVault : WelcomeScreenEvent

    data object OnLoadVault : WelcomeScreenEvent
}