package io.github.devapro.droid.importdata.model

import io.github.devapro.droid.data.vault.VaultItemModel
import io.github.vinceglb.filekit.PlatformFile

sealed interface ImportScreenState {
    data object Loading : ImportScreenState

    data class Error(val message: String) : ImportScreenState

    data class Loaded(
        val target: ImportTarget,
        val strategy: ImportStrategy,
        val activeVaultName: String,
        val canMergeIntoActive: Boolean,
        val newVaultName: String,
        val password: String,
        val isPasswordVisible: Boolean,
        val passwordError: String?,
        val selectedFormat: FileFormat,
        val isProcessing: Boolean,
        val formats: List<FormatModel>,
        val formatDescription: String,
        val pendingFile: PlatformFile?,
        val parsedItems: List<VaultItemModel>?,
        val conflictReport: ImportConflictReport?,
        val isConfirmDialogVisible: Boolean,
    ) : ImportScreenState
}
