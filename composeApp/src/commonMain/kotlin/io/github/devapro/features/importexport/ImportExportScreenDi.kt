package io.github.devapro.features.importexport

import io.github.devapro.features.importexport.factory.FormatsListFactory
import io.github.devapro.features.importexport.factory.ImportExportScreenInitStateFactory
import io.github.devapro.features.importexport.reducer.InitScreenReducer
import io.github.devapro.features.importexport.reducer.OnBackClickedReducer
import io.github.devapro.features.importexport.reducer.OnExportClickedReducer
import io.github.devapro.features.importexport.reducer.OnExportFileCancelledReducer
import io.github.devapro.features.importexport.reducer.OnExportFileSelectedReducer
import io.github.devapro.features.importexport.reducer.OnFormatSelectedReducer
import io.github.devapro.features.importexport.reducer.OnImportClickedReducer
import io.github.devapro.features.importexport.reducer.OnImportFileCancelledReducer
import io.github.devapro.features.importexport.reducer.OnImportFileSelectedReducer
import io.github.devapro.features.importexport.reducer.OnSwitchToExportReducer
import io.github.devapro.features.importexport.reducer.OnSwitchToImportReducer
import io.github.devapro.features.importexport.usecase.ImportFromCSVUseCase
import io.github.devapro.features.importexport.usecase.ImportFromDataUseCase
import io.github.devapro.features.importexport.usecase.ImportFromJsonUseCase
import io.github.devapro.features.importexport.usecase.SaveCSVFileUseCase
import io.github.devapro.features.importexport.usecase.SaveDataFileUseCase
import io.github.devapro.features.importexport.usecase.SaveJsonFileUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerImportExportScreenDi() {
    factoryOf(::ImportExportScreenViewModel)
    factoryOf(::ImportExportScreenInitStateFactory)
    factoryOf(::FormatsListFactory)
    reducersDi()
    useCaseDi()
}

private fun Module.useCaseDi() {
    factoryOf(::SaveCSVFileUseCase)
    factoryOf(::SaveDataFileUseCase)
    factoryOf(::SaveJsonFileUseCase)
    factoryOf(::ImportFromCSVUseCase)
    factoryOf(::ImportFromDataUseCase)
    factoryOf(::ImportFromJsonUseCase)
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnSwitchToImportReducer)
    factoryOf(::OnSwitchToExportReducer)
    factoryOf(::OnFormatSelectedReducer)
    factoryOf(::OnImportClickedReducer)
    factoryOf(::OnExportClickedReducer)
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnExportFileCancelledReducer)
    factoryOf(::OnExportFileSelectedReducer)
    factoryOf(::OnImportFileCancelledReducer)
    factoryOf(::OnImportFileSelectedReducer)
    
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
                get(OnExportFileCancelledReducer::class),
                get(OnExportFileSelectedReducer::class),
                get(OnImportFileCancelledReducer::class),
                get(OnImportFileSelectedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 