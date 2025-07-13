package io.github.devapro.features.importdata.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.features.importdata.model.ImportScreenAction
import io.github.devapro.features.importdata.model.ImportScreenEvent
import io.github.devapro.features.importdata.model.ImportScreenState
import io.github.vinceglb.filekit.dialogs.FileKitType

class OnImportClickedReducer
    :
    Reducer<ImportScreenAction.OnImportClicked, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.OnImportClicked::class

    override suspend fun reduce(
        action: ImportScreenAction.OnImportClicked,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction.OnImportClicked, ImportScreenEvent?> {
        val currentState = getState()
        return if (currentState is ImportScreenState.Loaded) {
            Reducer.Result(
                state = currentState.copy(isProcessing = true),
                action = null,
                event = ImportScreenEvent.OpenFileForImport(
                    type = FileKitType.File(setOf(currentState.selectedFormat.fileExtension))
                )
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }
} 