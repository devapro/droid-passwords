package io.github.devapro.ui.itemslist

import io.github.devapro.ui.itemslist.factory.PasswordListScreenInitStateFactory
import io.github.devapro.ui.itemslist.reducer.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerPasswordListScreenDi() {
    factoryOf(::PasswordListScreenViewModel)
    factoryOf(::PasswordListScreenInitStateFactory)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnSearchChangedReducer)
    factoryOf(::OnAddPasswordClickedReducer)
    factoryOf(::OnPasswordItemClickedReducer)
    factoryOf(::OnDeletePasswordClickedReducer)
    factoryOf(::OnImportExportClickedReducer)
    factoryOf(::OnSettingsClickedReducer)
    factoryOf(::OnRefreshReducer)
    factoryOf(::OnClearSearchReducer)
    
    factory {
        PasswordListScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnSearchChangedReducer::class),
                get(OnAddPasswordClickedReducer::class),
                get(OnPasswordItemClickedReducer::class),
                get(OnDeletePasswordClickedReducer::class),
                get(OnImportExportClickedReducer::class),
                get(OnSettingsClickedReducer::class),
                get(OnRefreshReducer::class),
                get(OnClearSearchReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 