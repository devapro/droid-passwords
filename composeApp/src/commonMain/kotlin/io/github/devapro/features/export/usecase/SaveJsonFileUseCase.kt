package io.github.devapro.features.export.usecase

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.writeString
import kotlinx.serialization.json.Json

class SaveJsonFileUseCase(
    private val repository: VaultRuntimeRepository,
    private val json: Json
) {

    suspend fun execute(
        file: PlatformFile
    ): AppResult<Unit> {
        return try {
            val vaultRawContent = json.encodeToString(repository.getVault())
            file.writeString(vaultRawContent)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Failure(e)
        }
    }
}