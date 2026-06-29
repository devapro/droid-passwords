package io.github.devapro.droid.unlock.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.unlock.model.UnLockVaultScreenAction
import io.github.devapro.droid.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.droid.unlock.model.UnLockVaultScreenState

class InitScreenReducer(
    private val registryRepository: VaultRegistryRepository,
) : Reducer<UnLockVaultScreenAction.InitScreen, UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent> {

    override val actionClass = UnLockVaultScreenAction.InitScreen::class

    override suspend fun reduce(
        action: UnLockVaultScreenAction.InitScreen,
        getState: () -> UnLockVaultScreenState
    ): Reducer.Result<UnLockVaultScreenState, UnLockVaultScreenAction.InitScreen, UnLockVaultScreenEvent?> {
        val descriptor = action.vaultId
            ?.let { registryRepository.getDescriptor(it) }
            ?: runCatching { registryRepository.requireActiveDescriptor() }.getOrNull()

        return Reducer.Result(
            state = UnLockVaultScreenState.Loaded(
                vaultId = descriptor?.id,
                vaultName = descriptor?.name.orEmpty(),
                password = "",
                isPasswordVisible = false,
                isProcessing = false,
                passwordError = null,
                isValid = false,
            ),
            action = null,
            event = null
        )
    }
}
