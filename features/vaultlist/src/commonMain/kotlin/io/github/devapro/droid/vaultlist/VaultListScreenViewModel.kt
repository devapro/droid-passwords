package io.github.devapro.droid.vaultlist

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenEvent
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

class VaultListScreenViewModel(
    actionProcessor: VaultListScreenActionProcessor,
) : MviViewModel<VaultListScreenState, VaultListScreenAction, VaultListScreenEvent>(
    actionProcessor,
)
