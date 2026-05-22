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

        val vault = runtimeRepository.getVault()
        vault.items.firstOrNull { it.id == action.item.id }?.let { existing ->
            runtimeRepository.addOrUpdateVault(existing.copy(lastUsedAt = now))
            fileRepository.saveVault(runtimeRepository.getVault())
        }

        return Reducer.Result(
            state = getState(),
            action = null,
            event = PasswordListScreenEvent.NavigateToPasswordDetail(updated)
        )
    }
} 