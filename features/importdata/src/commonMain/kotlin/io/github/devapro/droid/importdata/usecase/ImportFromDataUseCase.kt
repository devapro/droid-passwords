package io.github.devapro.droid.importdata.usecase

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.vinceglb.filekit.PlatformFile

class ImportFromDataUseCase(
    private val fileRepository: VaultFileRepository,
    private val repository: VaultRuntimeRepository
) {

    suspend fun execute(
        file: PlatformFile,
        password: String
    ): AppResult<Unit> {
        val model = fileRepository.getVault(
            fileForImport = file,
            password = password
        )
        return when (model) {
            is AppResult.Success -> {
                repository.loadVault(model.value)
                fileRepository.saveVault(model.value)
                AppResult.Success(Unit)
            }

            is AppResult.Failure -> AppResult.Failure(
                model.error
            )
        }
    }
}