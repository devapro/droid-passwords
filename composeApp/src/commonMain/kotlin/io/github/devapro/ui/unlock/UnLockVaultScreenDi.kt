package io.github.devapro.ui.unlock

import io.github.devapro.ui.unlock.factory.UnLockVaultScreenInitStateFactory
import io.github.devapro.ui.unlock.reducer.InitScreenReducer
import io.github.devapro.ui.unlock.reducer.OnBackClickedReducer
import io.github.devapro.ui.unlock.reducer.OnPasswordChangedReducer
import io.github.devapro.ui.unlock.reducer.OnTogglePasswordVisibilityReducer
import io.github.devapro.ui.unlock.reducer.OnUnlockClickedReducer
import io.github.devapro.ui.unlock.reducer.UnlockVaultReducer
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