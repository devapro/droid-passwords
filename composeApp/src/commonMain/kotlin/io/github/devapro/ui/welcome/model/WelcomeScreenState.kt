package io.github.devapro.ui.welcome.model

sealed interface WelcomeScreenState {
    data object Loading : WelcomeScreenState

    data class Error(val message: String) : WelcomeScreenState

    data class Success(
        val platformName: String,
        val isDarkThemeEnabled: Boolean,
        val isBiometricAuthEnabled: Boolean,
        val isPasswordProtected: Boolean,
    ) : WelcomeScreenState
}