package io.github.devapro.ui.welcome.model

sealed interface WelcomeScreenAction {
    data object InitScreen : WelcomeScreenAction

    data object OnCreateNewVault : WelcomeScreenAction

    data object OnLoadVault : WelcomeScreenAction

    data object OnOpenExistingVault : WelcomeScreenAction
}