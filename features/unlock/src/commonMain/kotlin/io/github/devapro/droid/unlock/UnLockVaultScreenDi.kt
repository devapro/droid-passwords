package io.github.devapro.droid.unlock

import io.github.devapro.droid.unlock.factory.UnLockVaultScreenFactoryImpl
import io.github.devapro.droid.unlock.factory.UnLockVaultScreenInitStateFactory
import io.github.devapro.droid.unlock.reducer.InitScreenReducer
import io.github.devapro.droid.unlock.reducer.OnBackClickedReducer
import io.github.devapro.droid.unlock.reducer.OnPasswordChangedReducer
import io.github.devapro.droid.unlock.reducer.OnTogglePasswordVisibilityReducer
import io.github.devapro.droid.unlock.reducer.OnUnlockClickedReducer
import io.github.devapro.droid.unlock.reducer.UnlockVaultReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerUnLockVaultScreenDi() {
    factoryOf(::UnLockVaultScreenViewModel)
    factoryOf(::UnLockVaultScreenInitStateFactory)
    factoryOf(::UnLockVaultScreenFactoryImpl).bind(UnLockVaultScreenFactory::class)
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