package io.github.devapro.features.importdata.model

import io.github.vinceglb.filekit.dialogs.FileKitType

sealed interface ImportScreenEvent {

    data object NavigateBack : ImportScreenEvent

    data class OpenFileFor(
        val fileName: String,
        val fileExtension: String
    ) : ImportScreenEvent

    data class OpenFileForImport(val type: FileKitType) : ImportScreenEvent

    data class ShowError(val message: String) : ImportScreenEvent

    data object ShowSuccess : ImportScreenEvent
} 