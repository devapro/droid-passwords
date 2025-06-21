package io.github.devapro.ui.setlock

import io.github.devapro.ui.setlock.factory.SetLockPasswordScreenInitStateFactory
import io.github.devapro.ui.setlock.reducer.*
import io.github.devapro.ui.welcome.reducer.OnCreateNewVaultReducer
import io.github.devapro.ui.welcome.reducer.OnOpenExistingVaultReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerSetLockPasswordScreenDi() {
    factoryOf(::SetLockPasswordScreenViewModel)
    factoryOf(::SetLockPasswordScreenInitStateFactory)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnCurrentPasswordChangedReducer)
    factoryOf(::OnNewPasswordChangedReducer)
    factoryOf(::OnConfirmPasswordChangedReducer)
    factoryOf(::OnToggleCurrentPasswordVisibilityReducer)
    factoryOf(::OnToggleNewPasswordVisibilityReducer)
    factoryOf(::OnToggleConfirmPasswordVisibilityReducer)
    factoryOf(::OnSaveClickedReducer)
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnRemovePasswordClickedReducer)
    
    factory {
        SetLockPasswordScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnCurrentPasswordChangedReducer::class),
                get(OnNewPasswordChangedReducer::class),
                get(OnConfirmPasswordChangedReducer::class),
                get(OnToggleCurrentPasswordVisibilityReducer::class),
                get(OnToggleNewPasswordVisibilityReducer::class),
                get(OnToggleConfirmPasswordVisibilityReducer::class),
                get(OnSaveClickedReducer::class),
                get(OnBackClickedReducer::class),
                get(OnRemovePasswordClickedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 