package io.github.devapro.features.importexport

import io.github.devapro.features.importexport.factory.FormatsListFactory
import io.github.devapro.features.importexport.factory.ImportExportScreenInitStateFactory
import io.github.devapro.features.importexport.reducer.InitScreenReducer
import io.github.devapro.features.importexport.reducer.OnBackClickedReducer
import io.github.devapro.features.importexport.reducer.OnExportClickedReducer
import io.github.devapro.features.importexport.reducer.OnExportStartedReducer
import io.github.devapro.features.importexport.reducer.OnFormatSelectedReducer
import io.github.devapro.features.importexport.reducer.OnImportClickedReducer
import io.github.devapro.features.importexport.reducer.OnSwitchToExportReducer
import io.github.devapro.features.importexport.reducer.OnSwitchToImportReducer
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
    factoryOf(::OnExportStartedReducer)
    
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
                get(OnExportStartedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 