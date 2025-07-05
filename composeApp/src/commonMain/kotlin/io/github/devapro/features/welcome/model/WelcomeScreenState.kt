package io.github.devapro.features.welcome.model

sealed interface WelcomeScreenState {
    data object Loading : WelcomeScreenState

    data class Error(val message: String) : WelcomeScreenState

    data class Success(
        val isVaultExists: Boolean
    ) : WelcomeScreenState
}