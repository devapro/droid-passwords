package io.github.devapro.droid.importdata.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.importdata.factory.FormatsListFactory
import io.github.devapro.droid.importdata.model.FileFormat
import io.github.devapro.droid.importdata.model.ImportScreenAction
import io.github.devapro.droid.importdata.model.ImportScreenEvent
import io.github.devapro.droid.importdata.model.ImportScreenState
import io.github.devapro.droid.importdata.model.ImportStrategy
import io.github.devapro.droid.importdata.model.ImportTarget

class InitScreenReducer(
    private val formatsListFactory: FormatsListFactory,
    private val registryRepository: VaultRegistryRepository,
    private val runtimeRepository: VaultRuntimeRepository,
) : Reducer<ImportScreenAction.InitScreen, ImportScreenState, ImportScreenAction, ImportScreenEvent> {

    override val actionClass = ImportScreenAction.InitScreen::class

    override suspend fun reduce(
        action: ImportScreenAction.InitScreen,
        getState: () -> ImportScreenState
    ): Reducer.Result<ImportScreenState, ImportScreenAction.InitScreen, ImportScreenEvent?> {
        val activeDescriptor = runCatching { runtimeRepository.getActiveDescriptor() }.getOrNull()
            ?: runCatching { registryRepository.requireActiveDescriptor() }.getOrNull()
        val canMerge = activeDescriptor != null && runCatching {
            runtimeRepository.getActiveVault()
        }.isSuccess
        return Reducer.Result(
            state = ImportScreenState.Loaded(
                target = if (canMerge) ImportTarget.MERGE_INTO_ACTIVE else ImportTarget.NEW_VAULT,
                strategy = ImportStrategy.MERGE_BY_TITLE_USERNAME,
                activeVaultName = activeDescriptor?.name.orEmpty(),
                canMergeIntoActive = canMerge,
                newVaultName = "",
                password = "",
                passwordError = null,
                isPasswordVisible = false,
                selectedFormat = FileFormat.JSON,
                isProcessing = false,
                formats = formatsListFactory.createFormatsList(),
                formatDescription = getFormatDescription(FileFormat.JSON),
                pendingFile = null,
                parsedItems = null,
                conflictReport = null,
                isConfirmDialogVisible = false,
            ),
            action = null,
            event = null
        )
    }

    private fun getFormatDescription(format: FileFormat): String {
        return when (format) {
            FileFormat.CSV -> "Comma-separated values format. Compatible with spreadsheet applications like Excel and Google Sheets. Simple and widely supported."
            FileFormat.JSON -> "JavaScript Object Notation format. Lightweight, easy to read, and commonly used for data exchange. Recommended for most use cases."
            FileFormat.DATA -> "Custom encrypted vault format used by this app. Use it to migrate or back up an entire vault."
        }
    }
}
