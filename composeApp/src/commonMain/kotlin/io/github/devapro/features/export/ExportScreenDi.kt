package io.github.devapro.features.export

import io.github.devapro.features.export.factory.ExportScreenInitStateFactory
import io.github.devapro.features.export.factory.FormatsListFactory
import io.github.devapro.features.export.reducer.InitScreenReducer
import io.github.devapro.features.export.reducer.OnBackClickedReducer
import io.github.devapro.features.export.reducer.OnExportClickedReducer
import io.github.devapro.features.export.reducer.OnExportFileCancelledReducer
import io.github.devapro.features.export.reducer.OnExportFileSelectedReducer
import io.github.devapro.features.export.reducer.OnFormatSelectedReducer
import io.github.devapro.features.export.usecase.SaveCSVFileUseCase
import io.github.devapro.features.export.usecase.SaveDataFileUseCase
import io.github.devapro.features.export.usecase.SaveJsonFileUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerExportScreenDi() {
    factoryOf(::ExportScreenViewModel)
    factoryOf(::ExportScreenInitStateFactory)
    factoryOf(::FormatsListFactory)
    reducersDi()
    useCaseDi()
}

private fun Module.useCaseDi() {
    factoryOf(::SaveCSVFileUseCase)
    factoryOf(::SaveDataFileUseCase)
    factoryOf(::SaveJsonFileUseCase)
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnFormatSelectedReducer)
    factoryOf(::OnExportClickedReducer)
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnExportFileCancelledReducer)
    factoryOf(::OnExportFileSelectedReducer)

    factory {
        ExportScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnFormatSelectedReducer::class),
                get(OnExportClickedReducer::class),
                get(OnBackClickedReducer::class),
                get(OnExportFileCancelledReducer::class),
                get(OnExportFileSelectedReducer::class)
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 