package io.github.devapro.features.setlock

import io.github.devapro.droid.setlock.SetLockPasswordScreenFactory
import io.github.devapro.features.setlock.factory.SetLockPasswordScreenFactoryImpl
import io.github.devapro.features.setlock.factory.SetLockPasswordScreenInitStateFactory
import io.github.devapro.features.setlock.reducer.InitScreenReducer
import io.github.devapro.features.setlock.reducer.OnBackClickedReducer
import io.github.devapro.features.setlock.reducer.OnConfirmPasswordChangedReducer
import io.github.devapro.features.setlock.reducer.OnCurrentPasswordChangedReducer
import io.github.devapro.features.setlock.reducer.OnNewPasswordChangedReducer
import io.github.devapro.features.setlock.reducer.OnRemovePasswordClickedReducer
import io.github.devapro.features.setlock.reducer.OnSaveClickedReducer
import io.github.devapro.features.setlock.reducer.OnToggleConfirmPasswordVisibilityReducer
import io.github.devapro.features.setlock.reducer.OnToggleCurrentPasswordVisibilityReducer
import io.github.devapro.features.setlock.reducer.OnToggleNewPasswordVisibilityReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerSetLockPasswordScreenDi() {
    factoryOf(::SetLockPasswordScreenViewModel)
    factoryOf(::SetLockPasswordScreenInitStateFactory)
    factoryOf(::SetLockPasswordScreenFactoryImpl).bind(SetLockPasswordScreenFactory::class)
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