package io.github.devapro.features.settings.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.SettingsRepository
import io.github.devapro.features.settings.model.SettingsScreenAction
import io.github.devapro.features.settings.model.SettingsScreenEvent
import io.github.devapro.features.settings.model.SettingsScreenState

/**
 * Reducer for handling file path selection.
 * Validates the selected path, performs safe file migration, and updates the vault file path.
 */
class OnFilePathSelectedReducer(
    private val settingsRepository: SettingsRepository
) : Reducer<SettingsScreenAction.OnFilePathSelected, SettingsScreenState, SettingsScreenAction, SettingsScreenEvent> {

    override val actionClass = SettingsScreenAction.OnFilePathSelected::class

    override suspend fun reduce(
        action: SettingsScreenAction.OnFilePathSelected,
        getState: () -> SettingsScreenState
    ): Reducer.Result<SettingsScreenState, SettingsScreenAction, SettingsScreenEvent?> {
        val currentState = getState()

        return if (currentState is SettingsScreenState.Success) {
            // Start loading state
            val loadingState = currentState.copy(
                isChangingFilePath = true,
                filePathChangeError = null
            )

            // Validate the selected path
            val validationError = validateFilePath(action.path)
            if (validationError != null) {
                return Reducer.Result(
                    state = currentState.copy(
                        isChangingFilePath = false,
                        filePathChangeError = validationError
                    ),
                    action = null,
                    event = null
                )
            }

            // Check if the path is the same as current path
            if (action.path == currentState.vaultFilePath) {
                return Reducer.Result(
                    state = currentState.copy(
                        isFilePathDialogVisible = false,
                        filePathChangeError = null
                    ),
                    action = null,
                    event = SettingsScreenEvent.ShowSuccess("File path unchanged")
                )
            }

            // Attempt to set the new vault file path (includes validation and migration)
            when (val result = settingsRepository.setVaultFilePath(action.path)) {
                is AppResult.Success -> {
                    Reducer.Result(
                        state = currentState.copy(
                            vaultFilePath = action.path,
                            isChangingFilePath = false,
                            isFilePathDialogVisible = false,
                            filePathChangeError = null
                        ),
                        action = null,
                        event = SettingsScreenEvent.ShowSuccess("Vault file path updated successfully")
                    )
                }

                is AppResult.Failure -> {
                    val errorMessage = result.error.message ?: "Failed to update vault file path"
                    Reducer.Result(
                        state = currentState.copy(
                            isChangingFilePath = false,
                            filePathChangeError = errorMessage
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
                event = SettingsScreenEvent.ShowError("Cannot change file path in current state")
            )
        }
    }

    /**
     * Validates the selected file path.
     * @param path The file path to validate
     * @return Error message if validation fails, null if validation passes
     */
    private fun validateFilePath(path: String): String? {
        return when {
            path.isEmpty() -> "File path cannot be empty"
            path.length > 500 -> "File path is too long (maximum 500 characters)"
            path.contains("..") -> "File path cannot contain relative path components (..)"
            // Additional platform-specific validations could be added here
            else -> null
        }
    }
}