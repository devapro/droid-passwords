package io.github.devapro.droid.welcome.model

sealed interface WelcomeScreenAction {
    data object InitScreen : WelcomeScreenAction

    data object OnCreateNewVault : WelcomeScreenAction

    data object OnLoadVault : WelcomeScreenAction

    data object OnOpenExistingVault : WelcomeScreenAction

    data object OnRestoreFromSyncClicked : WelcomeScreenAction

    data object OnDismissRestoreAuthDialog : WelcomeScreenAction

    data class OnRestoreLoginSubmitted(
        val url: String,
        val username: String,
        val password: String
    ) : WelcomeScreenAction

    data class OnRestoreRegisterSubmitted(
        val url: String,
        val username: String,
        val password: String
    ) : WelcomeScreenAction

    data object OnDismissMasterPasswordDialog : WelcomeScreenAction

    data class OnRestoreMasterPasswordSubmitted(val masterPassword: String) : WelcomeScreenAction
}
