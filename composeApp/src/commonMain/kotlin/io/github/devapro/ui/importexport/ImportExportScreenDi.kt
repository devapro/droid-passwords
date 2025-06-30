package io.github.devapro.ui.importexport

import io.github.devapro.ui.importexport.factory.FormatsListFactory
import io.github.devapro.ui.importexport.factory.ImportExportScreenInitStateFactory
import io.github.devapro.ui.importexport.reducer.InitScreenReducer
import io.github.devapro.ui.importexport.reducer.OnBackClickedReducer
import io.github.devapro.ui.importexport.reducer.OnExportClickedReducer
import io.github.devapro.ui.importexport.reducer.OnFormatSelectedReducer
import io.github.devapro.ui.importexport.reducer.OnImportClickedReducer
import io.github.devapro.ui.importexport.reducer.OnSwitchToExportReducer
import io.github.devapro.ui.importexport.reducer.OnSwitchToImportReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerImportExportScreenDi() {
    factoryOf(::ImportExportScreenViewModel)
    factoryOf(::ImportExportScreenInitStateFactory)
    factoryOf(::FormatsListFactory)
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