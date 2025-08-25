package io.github.devapro.features.importdata

import io.github.devapro.droid.importdata.ImportScreenFactory
import io.github.devapro.features.importdata.factory.FormatsListFactory
import io.github.devapro.features.importdata.factory.ImportScreenFactoryImpl
import io.github.devapro.features.importdata.factory.ImportScreenInitStateFactory
import io.github.devapro.features.importdata.reducer.InitScreenReducer
import io.github.devapro.features.importdata.reducer.OnBackClickedReducer
import io.github.devapro.features.importdata.reducer.OnFormatSelectedReducer
import io.github.devapro.features.importdata.reducer.OnImportClickedReducer
import io.github.devapro.features.importdata.reducer.OnImportFileCancelledReducer
import io.github.devapro.features.importdata.reducer.OnImportFileSelectedReducer
import io.github.devapro.features.importdata.reducer.OnPasswordChangedReducer
import io.github.devapro.features.importdata.reducer.OnTogglePasswordVisibilityReducer
import io.github.devapro.features.importdata.usecase.ImportFromCSVUseCase
import io.github.devapro.features.importdata.usecase.ImportFromDataUseCase
import io.github.devapro.features.importdata.usecase.ImportFromJsonUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerImportScreenDi() {
    factoryOf(::ImportScreenViewModel)
    factoryOf(::ImportScreenInitStateFactory)
    factoryOf(::FormatsListFactory)
    factoryOf(::ImportScreenFactoryImpl).bind(ImportScreenFactory::class)
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
    factoryOf(::OnPasswordChangedReducer)
    factoryOf(::OnTogglePasswordVisibilityReducer)
    
    factory {
        ImportActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnFormatSelectedReducer::class),
                get(OnImportClickedReducer::class),
                get(OnBackClickedReducer::class),
                get(OnImportFileCancelledReducer::class),
                get(OnImportFileSelectedReducer::class),
                get(OnPasswordChangedReducer::class),
                get(OnTogglePasswordVisibilityReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 