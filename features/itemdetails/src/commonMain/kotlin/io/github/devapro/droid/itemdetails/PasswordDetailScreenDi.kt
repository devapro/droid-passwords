package io.github.devapro.droid.itemdetails

import io.github.devapro.droid.itemdetails.factory.PasswordDetailScreenInitStateFactory
import io.github.devapro.droid.itemdetails.navigation.PasswordDetailScreenFactoryImpl
import io.github.devapro.droid.itemdetails.reducer.InitScreenReducer
import io.github.devapro.droid.itemdetails.reducer.OnBackClickedReducer
import io.github.devapro.droid.itemdetails.reducer.OnCopyFieldReducer
import io.github.devapro.droid.itemdetails.reducer.OnDeleteCancelledReducer
import io.github.devapro.droid.itemdetails.reducer.OnDeleteClickedReducer
import io.github.devapro.droid.itemdetails.reducer.OnDeleteConfirmedReducer
import io.github.devapro.droid.itemdetails.reducer.OnEditClickedReducer
import io.github.devapro.droid.itemdetails.reducer.OnShareClickedReducer
import io.github.devapro.droid.itemdetails.reducer.OnTogglePasswordVisibilityReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerPasswordDetailScreenDi() {
    factoryOf(::PasswordDetailScreenViewModel)
    factoryOf(::PasswordDetailScreenInitStateFactory)
    factoryOf(::PasswordDetailScreenFactoryImpl).bind(PasswordDetailScreenFactory::class)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnEditClickedReducer)
    factoryOf(::OnDeleteClickedReducer)
    factoryOf(::OnDeleteConfirmedReducer)
    factoryOf(::OnDeleteCancelledReducer)
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
                get(OnDeleteConfirmedReducer::class),
                get(OnDeleteCancelledReducer::class),
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