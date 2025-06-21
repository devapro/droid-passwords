package io.github.devapro.ui.unlock

import io.github.devapro.core.mvi.ActionProcessor
import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.data.LockManager
import io.github.devapro.ui.unlock.factory.UnLockVaultScreenInitStateFactory
import io.github.devapro.ui.unlock.model.UnLockVaultScreenAction
import io.github.devapro.ui.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.ui.unlock.model.UnLockVaultScreenState

class UnLockVaultScreenActionProcessor(
    private val initStateFactory: UnLockVaultScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<UnLockVaultScreenAction, UnLockVaultScreenState, UnLockVaultScreenAction, UnLockVaultScreenEvent>>,
) : ActionProcessor<
        UnLockVaultScreenState,
        UnLockVaultScreenAction,
        UnLockVaultScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default
) 