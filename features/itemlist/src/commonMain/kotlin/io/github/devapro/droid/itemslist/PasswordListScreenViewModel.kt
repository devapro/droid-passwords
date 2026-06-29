package io.github.devapro.droid.itemslist

import androidx.lifecycle.viewModelScope
import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.itemslist.model.PasswordListScreenAction
import io.github.devapro.droid.itemslist.model.PasswordListScreenEvent
import io.github.devapro.droid.itemslist.model.PasswordListScreenState
import kotlinx.coroutines.launch

class PasswordListScreenViewModel(
    actionProcessor: PasswordListScreenActionProcessor,
    runtimeRepository: VaultRuntimeRepository,
) : MviViewModel<PasswordListScreenState, PasswordListScreenAction, PasswordListScreenEvent>(
    actionProcessor = actionProcessor
) {
    init {
        viewModelScope.launch {
            runtimeRepository.activeVaultChanges.collect {
                onAction(PasswordListScreenAction.RefreshList)
            }
        }
    }
}
