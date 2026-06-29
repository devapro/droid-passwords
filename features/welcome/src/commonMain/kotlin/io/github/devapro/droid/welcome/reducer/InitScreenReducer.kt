package io.github.devapro.droid.welcome.reducer

import io.github.devapro.droid.core.mvi.AppResult
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.sync.SyncScheduler
import io.github.devapro.droid.data.vault.VaultDescriptor
import io.github.devapro.droid.data.sync.SyncStateStore
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.welcome.model.WelcomeScreenAction
import io.github.devapro.droid.welcome.model.WelcomeScreenEvent
import io.github.devapro.droid.welcome.model.WelcomeScreenState

class InitScreenReducer(
    private val vaultFileRepository: VaultFileRepository,
    private val syncStateStore: SyncStateStore,
    private val vaultRegistryRepository: VaultRegistryRepository,
    private val syncScheduler: SyncScheduler,
) : Reducer<WelcomeScreenAction.InitScreen, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.InitScreen::class

    override suspend fun reduce(
        action: WelcomeScreenAction.InitScreen,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent?> {
        val registry = vaultRegistryRepository.getRegistry()
        val hasRegistry = registry.isNotEmpty()
        val hasLegacy = vaultFileRepository.legacyVaultExists()

        if (!hasRegistry && hasLegacy) {
            // First launch after upgrade: register the existing droid-d4.data as "Default vault"
            // and move it into the durable app data directory.
            val now = nowMillis()
            val id = VaultDescriptor.newId()
            val descriptor = VaultDescriptor(
                id = id,
                name = "Default vault",
                fileName = VaultDescriptor.newFileName(id),
                createdAt = now,
                updatedAt = now,
            )
            val migrationResult = vaultFileRepository.migrateLegacyVault(descriptor)
            if (migrationResult is AppResult.Success) {
                vaultRegistryRepository.addVault(descriptor)
                vaultRegistryRepository.setActiveVaultId(descriptor.id)
            }
        }

        // Signed in: refresh the account's vault list on every launch so any vault created
        // on another device shows up as a selectable (locked) entry. Runs in the background
        // (never blocks startup); the vault list reflects the result when it is opened.
        syncScheduler.syncVaultListOnStart()

        val finalRegistry = vaultRegistryRepository.getRegistry()
        return Reducer.Result(
            state = WelcomeScreenState.Success(
                isVaultExists = finalRegistry.isNotEmpty(),
                syncServerUrl = syncStateStore.getServerUrl()
            ),
            action = null,
            event = null
        )
    }

    private fun nowMillis(): Long {
        @OptIn(kotlin.time.ExperimentalTime::class)
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }
}
