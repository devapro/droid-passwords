package io.github.devapro.ui.itemdetails

import io.github.devapro.ui.itemdetails.factory.PasswordDetailScreenInitStateFactory
import io.github.devapro.ui.itemdetails.reducer.*
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
    factoryOf(::OnFavoriteClickedReducer)
    
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
                get(OnFavoriteClickedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 