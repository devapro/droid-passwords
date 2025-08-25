package io.github.devapro.droid.welcome.model

sealed interface WelcomeScreenEvent {

    data object OnCreateNewVault : WelcomeScreenEvent

    data object OnOpenExistingVault : WelcomeScreenEvent

    data object OnLoadVault : WelcomeScreenEvent
}