package io.github.devapro.features.importdata

import io.github.devapro.features.importdata.factory.FormatsListFactory
import io.github.devapro.features.importdata.factory.ImportScreenInitStateFactory
import io.github.devapro.features.importdata.reducer.InitScreenReducer
import io.github.devapro.features.importdata.reducer.OnBackClickedReducer
import io.github.devapro.features.importdata.reducer.OnFormatSelectedReducer
import io.github.devapro.features.importdata.reducer.OnImportClickedReducer
import io.github.devapro.features.importdata.reducer.OnImportFileCancelledReducer
import io.github.devapro.features.importdata.reducer.OnImportFileSelectedReducer
import io.github.devapro.features.importdata.usecase.ImportFromCSVUseCase
import io.github.devapro.features.importdata.usecase.ImportFromDataUseCase
import io.github.devapro.features.importdata.usecase.ImportFromJsonUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerImportScreenDi() {
    factoryOf(::ImportScreenViewModel)
    factoryOf(::ImportScreenInitStateFactory)
    factoryOf(::FormatsListFactory)
    reducersDi()
    useCaseDi()
}

private fun Module.useCaseDi() {
    factoryOf(::ImportFromCSVUseCase)
    factoryOf(::ImportFromDataUseCase)
    factoryOf(::ImportFromJsonUseCase)
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnFormatSelectedReducer)
    factoryOf(::OnImportClickedReducer)
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnImportFileCancelledReducer)
    factoryOf(::OnImportFileSelectedReducer)
    
    factory {
        ImportActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnFormatSelectedReducer::class),
                get(OnImportClickedReducer::class),
                get(OnBackClickedReducer::class),
                get(OnImportFileCancelledReducer::class),
                get(OnImportFileSelectedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 