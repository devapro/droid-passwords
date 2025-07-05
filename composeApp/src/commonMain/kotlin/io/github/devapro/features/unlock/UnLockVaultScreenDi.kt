package io.github.devapro.features.unlock

import io.github.devapro.features.unlock.factory.UnLockVaultScreenInitStateFactory
import io.github.devapro.features.unlock.reducer.InitScreenReducer
import io.github.devapro.features.unlock.reducer.OnBackClickedReducer
import io.github.devapro.features.unlock.reducer.OnPasswordChangedReducer
import io.github.devapro.features.unlock.reducer.OnTogglePasswordVisibilityReducer
import io.github.devapro.features.unlock.reducer.OnUnlockClickedReducer
import io.github.devapro.features.unlock.reducer.UnlockVaultReducer
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
    factoryOf(::UnlockVaultReducer)
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
                get(UnlockVaultReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 