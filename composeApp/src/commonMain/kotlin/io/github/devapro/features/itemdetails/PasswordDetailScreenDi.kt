package io.github.devapro.features.itemdetails

import io.github.devapro.features.itemdetails.factory.PasswordDetailScreenInitStateFactory
import io.github.devapro.features.itemdetails.reducer.InitScreenReducer
import io.github.devapro.features.itemdetails.reducer.OnBackClickedReducer
import io.github.devapro.features.itemdetails.reducer.OnCopyFieldReducer
import io.github.devapro.features.itemdetails.reducer.OnDeleteClickedReducer
import io.github.devapro.features.itemdetails.reducer.OnEditClickedReducer
import io.github.devapro.features.itemdetails.reducer.OnShareClickedReducer
import io.github.devapro.features.itemdetails.reducer.OnTogglePasswordVisibilityReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerPasswordDetailScreenDi() {
    factoryOf(::PasswordDetailScreenViewModel)
    factoryOf(::PasswordDetailScreenInitStateFactory)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnEditClickedReducer)
    factoryOf(::OnDeleteClickedReducer)
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnTogglePasswordVisibilityReducer)
    factoryOf(::OnCopyFieldReducer)
    factoryOf(::OnShareClickedReducer)
    
    factory {
        PasswordDetailScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnEditClickedReducer::class),
                get(OnDeleteClickedReducer::class),
                get(OnBackClickedReducer::class),
                get(OnTogglePasswordVisibilityReducer::class),
                get(OnCopyFieldReducer::class),
                get(OnShareClickedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 