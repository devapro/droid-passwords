package io.github.devapro.ui.importexport

import io.github.devapro.ui.importexport.factory.ImportExportScreenInitStateFactory
import io.github.devapro.ui.importexport.reducer.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerImportExportScreenDi() {
    factoryOf(::ImportExportScreenViewModel)
    factoryOf(::ImportExportScreenInitStateFactory)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnSwitchToImportReducer)
    factoryOf(::OnSwitchToExportReducer)
    factoryOf(::OnFormatSelectedReducer)
    factoryOf(::OnImportClickedReducer)
    factoryOf(::OnExportClickedReducer)
    factoryOf(::OnBackClickedReducer)
    
    factory {
        ImportExportScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnSwitchToImportReducer::class),
                get(OnSwitchToExportReducer::class),
                get(OnFormatSelectedReducer::class),
                get(OnImportClickedReducer::class),
                get(OnExportClickedReducer::class),
                get(OnBackClickedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 