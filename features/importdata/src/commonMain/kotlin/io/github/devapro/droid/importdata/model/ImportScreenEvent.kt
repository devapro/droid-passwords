package io.github.devapro.droid.importdata.model

import io.github.vinceglb.filekit.dialogs.FileKitType

sealed interface ImportScreenEvent {

    data object NavigateBack : ImportScreenEvent

    data class OpenFileForImport(val type: FileKitType) : ImportScreenEvent

    data class ShowError(val message: String) : ImportScreenEvent

    data class ShowSuccess(val message: String) : ImportScreenEvent
}
