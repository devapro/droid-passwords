package io.github.devapro.ui.unlock

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.ui.unlock.model.UnLockVaultScreenAction
import io.github.devapro.ui.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.ui.unlock.model.UnLockVaultScreenState

class UnLockVaultScreenViewModel(
    actionProcessor: UnLockVaultScreenActionProcessor,
) : MviViewModel<UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent>(
    actionProcessor = actionProcessor
) 