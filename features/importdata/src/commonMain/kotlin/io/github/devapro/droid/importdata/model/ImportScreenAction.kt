package io.github.devapro.droid.importdata.model

import io.github.vinceglb.filekit.PlatformFile

sealed interface ImportScreenAction {
    data object InitScreen : ImportScreenAction

    data class OnFormatSelected(val format: FormatModel) : ImportScreenAction

    data class OnTargetSelected(val target: ImportTarget) : ImportScreenAction

    data class OnStrategySelected(val strategy: ImportStrategy) : ImportScreenAction

    data class OnNewVaultNameChanged(val name: String) : ImportScreenAction

    data object OnImportClicked : ImportScreenAction

    data object OnBackClicked : ImportScreenAction

    data class ImportFileSelected(val file: PlatformFile) : ImportScreenAction

    data object ImportFileCancelled : ImportScreenAction

    data class OnPasswordChanged(val password: String) : ImportScreenAction

    data object OnTogglePasswordVisibility : ImportScreenAction

    data object OnConfirmImportPassword : ImportScreenAction

    data object OnDismissPasswordDialog : ImportScreenAction

    data object OnConfirmImport : ImportScreenAction

    data object OnDismissConfirmDialog : ImportScreenAction
}
