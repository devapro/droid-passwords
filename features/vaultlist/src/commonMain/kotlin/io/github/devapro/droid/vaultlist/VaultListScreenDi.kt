package io.github.devapro.droid.vaultlist

import io.github.devapro.droid.vaultlist.factory.VaultListScreenFactoryImpl
import io.github.devapro.droid.vaultlist.factory.VaultListScreenInitStateFactory
import io.github.devapro.droid.vaultlist.reducer.InitScreenReducer
import io.github.devapro.droid.vaultlist.reducer.OnAddVaultClickedReducer
import io.github.devapro.droid.vaultlist.reducer.OnBackClickedReducer
import io.github.devapro.droid.vaultlist.reducer.OnCreateNewVaultSelectedReducer
import io.github.devapro.droid.vaultlist.reducer.OnDismissAddMenuReducer
import io.github.devapro.droid.vaultlist.reducer.OnImportVaultSelectedReducer
import io.github.devapro.droid.vaultlist.reducer.OnLogoutClickedReducer
import io.github.devapro.droid.vaultlist.reducer.OnRefreshReducer
import io.github.devapro.droid.vaultlist.reducer.OnVaultClickedReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerVaultListScreenDi() {
    factoryOf(::VaultListScreenViewModel)
    factoryOf(::VaultListScreenInitStateFactory)
    factoryOf(::VaultListScreenFactoryImpl).bind(VaultListScreenFactory::class)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnRefreshReducer)
    factoryOf(::OnVaultClickedReducer)
    factoryOf(::OnAddVaultClickedReducer)
    factoryOf(::OnDismissAddMenuReducer)
    factoryOf(::OnCreateNewVaultSelectedReducer)
    factoryOf(::OnImportVaultSelectedReducer)
    factoryOf(::OnLogoutClickedReducer)
    factoryOf(::OnBackClickedReducer)

    factory {
        VaultListScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnRefreshReducer::class),
                get(OnVaultClickedReducer::class),
                get(OnAddVaultClickedReducer::class),
                get(OnDismissAddMenuReducer::class),
                get(OnCreateNewVaultSelectedReducer::class),
                get(OnImportVaultSelectedReducer::class),
                get(OnLogoutClickedReducer::class),
                get(OnBackClickedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get(),
        )
    }
}
