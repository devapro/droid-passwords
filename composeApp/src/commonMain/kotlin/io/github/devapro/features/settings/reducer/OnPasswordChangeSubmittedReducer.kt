package io.github.devapro.features.settings.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.SettingsRepository
import io.github.devapro.features.settings.model.SettingsScreenAction
import io.github.devapro.features.settings.model.SettingsScreenEvent
import io.github.devapro.features.settings.model.SettingsScreenState

/**
 * Reducer for handling password change submission.
 * Validates the password inputs and delegates to SettingsRepository for the actual password change.
 */
class OnPasswordChangeSubmittedReducer(
    private val settingsRepository: SettingsRepository
) : Reducer<SettingsScreenAction.OnPasswordChangeSubmitted, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnPasswordChangeSubmitted::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnPasswordChangeSubmitted,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()

        return if (currentState is SettingsScreenState.Success) {
            // Start loading state
            val loadingState = currentState.copy(
                isChangingPassword = true,
                passwordChangeError = null
            )

            // Validate password inputs
            val validationError = validatePasswordInputs(
                action.currentPassword,
                action.newPassword,
                action.confirmPassword
            )

            if (validationError != null) {
                return Reducer.Result(
                    state = currentState.copy(
                        isChangingPassword = false,
                        passwordChangeError = validationError
                    ),
                    action = null,
                    event = null
                )
            }

            // Attempt to change the password
            when (val result = settingsRepository.changeVaultPassword(
                action.currentPassword,
                action.newPassword
            )) {
                is AppResult.Success -> {
                    Reducer.Result(
                        state = currentState.copy(
                            isChangingPassword = false,
                            isChangePasswordDialogVisible = false,
                            passwordChangeError = null
                        ),
                        action = null,
                        event = SettingsScreenEvent.ShowSuccess("Password changed successfully")
                    )
                }

                is AppResult.Failure -> {
                    val errorMessage = result.error.message ?: "Failed to change password"
                    Reducer.Result(
                        state = currentState.copy(
                            isChangingPassword = false,
                            passwordChangeError = errorMessage
                        ),
                        action = null,
                        event = null
                    )
                }
            }
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = SettingsScreenEvent.ShowError("Cannot change password in current state")
            )
        }
    }

    /**
     * Validates the password change inputs.
     * @param currentPassword The current password
     * @param newPassword The new password
     * @param confirmPassword The password confirmation
     * @return Error message if validation fails, null if validation passes
     */
    private fun validatePasswordInputs(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): String? {
        return when {
            currentPassword.isEmpty() -> "Current password cannot be empty"
            newPassword.isEmpty() -> "New password cannot be empty"
            confirmPassword.isEmpty() -> "Password confirmation cannot be empty"
            newPassword != confirmPassword -> "New password and confirmation do not match"
            currentPassword == newPassword -> "New password must be different from current password"
            newPassword.length < 4 -> "New password must be at least 4 characters long"
            else -> null
        }
    }
}