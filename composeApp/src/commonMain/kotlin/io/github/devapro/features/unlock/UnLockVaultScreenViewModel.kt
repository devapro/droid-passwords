package io.github.devapro.features.unlock

import io.github.devapro.core.mvi.MviViewModel
import io.github.devapro.features.unlock.model.UnLockVaultScreenAction
import io.github.devapro.features.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.features.unlock.model.UnLockVaultScreenState

class UnLockVaultScreenViewModel(
    actionProcessor: UnLockVaultScreenActionProcessor,
) : MviViewModel<UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent>(
    actionProcessor = actionProcessor
) 