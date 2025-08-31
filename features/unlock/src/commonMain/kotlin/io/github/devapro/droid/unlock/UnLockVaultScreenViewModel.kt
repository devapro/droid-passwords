package io.github.devapro.droid.unlock

import io.github.devapro.droid.core.mvi.MviViewModel
import io.github.devapro.droid.unlock.model.UnLockVaultScreenAction
import io.github.devapro.droid.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.droid.unlock.model.UnLockVaultScreenState

class UnLockVaultScreenViewModel(
    actionProcessor: UnLockVaultScreenActionProcessor,
) : MviViewModel<UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent>(
    actionProcessor = actionProcessor
) 