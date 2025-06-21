package io.github.devapro.ui.unlock

import io.github.devapro.ui.unlock.factory.UnLockVaultScreenInitStateFactory
import io.github.devapro.ui.unlock.reducer.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerUnLockVaultScreenDi() {
    factoryOf(::UnLockVaultScreenViewModel)
    factoryOf(::UnLockVaultScreenInitStateFactory)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnPasswordChangedReducer)
    factoryOf(::OnTogglePasswordVisibilityReducer)
    factoryOf(::OnUnlockClickedReducer)
    factoryOf(::OnBackClickedReducer)
    
    factory {
        UnLockVaultScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnPasswordChangedReducer::class),
                get(OnTogglePasswordVisibilityReducer::class),
                get(OnUnlockClickedReducer::class),
                get(OnBackClickedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 