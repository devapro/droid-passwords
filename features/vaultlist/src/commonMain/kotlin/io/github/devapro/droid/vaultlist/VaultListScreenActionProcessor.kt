package io.github.devapro.droid.vaultlist

import io.github.devapro.droid.core.mvi.ActionProcessor
import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.vaultlist.factory.VaultListScreenInitStateFactory
import io.github.devapro.droid.vaultlist.model.VaultListScreenAction
import io.github.devapro.droid.vaultlist.model.VaultListScreenEvent
import io.github.devapro.droid.vaultlist.model.VaultListScreenState

class VaultListScreenActionProcessor(
    private val initStateFactory: VaultListScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<VaultListScreenAction, VaultListScreenState, VaultListScreenAction, VaultListScreenEvent>>,
) : ActionProcessor<
        VaultListScreenState,
        VaultListScreenAction,
        VaultListScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default,
)
