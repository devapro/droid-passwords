package io.github.devapro.droid.export.usecase

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.vinceglb.filekit.PlatformFile

class SaveDataFileUseCase(
    private val fileRepository: VaultFileRepository,
    private val repository: VaultRuntimeRepository
) {

    suspend fun execute(
        file: PlatformFile
    ): AppResult<Unit> {
        return fileRepository.saveVault(
            vaultModel = repository.getVault(),
            fileForExport = file
        )
    }
}