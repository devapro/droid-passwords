package io.github.devapro.droid.importdata.model

import io.github.vinceglb.filekit.PlatformFile

sealed interface ImportScreenState {
    data object Loading : ImportScreenState

    data class Error(val message: String) : ImportScreenState

    data class Loaded(
        val password: String,
        val isPasswordVisible: Boolean,
        val passwordError: String?,
        val selectedFormat: FileFormat,
        val isProcessing: Boolean,
        val formats: List<FormatModel>,
        val formatDescription: String,
        val pendingFile: PlatformFile?
    ) : ImportScreenState
}
