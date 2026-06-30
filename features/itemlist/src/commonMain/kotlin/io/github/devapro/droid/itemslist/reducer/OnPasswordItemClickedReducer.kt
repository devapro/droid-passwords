package io.github.devapro.droid.itemslist.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenEvent
import io.github.devapro.droid.itemslist.model.PasswordListScreenState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class OnPasswordItemClickedReducer(
    private val runtimeRepository: VaultRuntimeRepository,
    private val fileRepository: VaultFileRepository
)    : Reducer<PasswordListScreenAction.OnPasswordItemClicked, PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent> {

    override val actionClass = PasswordListScreenAction.OnPasswordItemClicked::class

    @OptIn(ExperimentalTime::class)
    override suspend fun reduce(
        action: PasswordListScreenAction.OnPasswordItemClicked,
        getState: () -> PasswordListScreenState
    ): Reducer.Result<PasswordListScreenState, PasswordListScreenAction.OnPasswordItemClicked, PasswordListScreenEvent?> {
        val now = Clock.System.now().toEpochMilliseconds()
        val updated = action.item.copy(lastUsedAt = now)

        val vault = runtimeRepository.getActiveVaultOrNull()
        vault?.items?.firstOrNull { it.id == action.item.id }?.let { existing ->
            // Bump lastUsedAt without marking the item dirty — a mere view must not
            // trigger a sync upload on every tap.
            runtimeRepository.updateItemMetadata(existing.copy(lastUsedAt = now))
            // Read descriptor + contents atomically so a concurrent background sync can
            // never pair this vault's descriptor with another vault's items on disk.
            runtimeRepository.getActiveSnapshot()?.let { snapshot ->
                fileRepository.saveVault(descriptor = snapshot.descriptor, vaultModel = snapshot.vault)
            }
        }

        return Reducer.Result(
            state = getState(),
            action = null,
            event = PasswordListScreenEvent.NavigateToPasswordDetail(updated)
        )
    }
} 