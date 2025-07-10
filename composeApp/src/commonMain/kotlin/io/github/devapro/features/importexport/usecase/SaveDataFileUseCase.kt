package io.github.devapro.features.importexport.usecase

import io.github.devapro.core.mvi.AppResult
import io.github.devapro.data.vault.VaultFileRepository
import io.github.devapro.data.vault.VaultRuntimeRepository
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