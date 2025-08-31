package io.github.devapro.droid.unlock

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.unlock.factory.UnLockVaultScreenInitStateFactory
import io.github.devapro.droid.unlock.model.UnLockVaultScreenAction
import io.github.devapro.droid.unlock.model.UnLockVaultScreenEvent
import io.github.devapro.droid.unlock.model.UnLockVaultScreenState

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