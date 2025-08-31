package io.github.devapro.droid.export

import io.github.devapro.droid.export.factory.ExportScreenInitStateFactory
import io.github.devapro.droid.export.factory.FormatsListFactory
import io.github.devapro.droid.export.navigation.ExportScreenFactoryImpl
import io.github.devapro.droid.export.reducer.InitScreenReducer
import io.github.devapro.droid.export.reducer.OnBackClickedReducer
import io.github.devapro.droid.export.reducer.OnExportClickedReducer
import io.github.devapro.droid.export.reducer.OnExportFileCancelledReducer
import io.github.devapro.droid.export.reducer.OnExportFileSelectedReducer
import io.github.devapro.droid.export.reducer.OnFormatSelectedReducer
import io.github.devapro.droid.export.usecase.SaveCSVFileUseCase
import io.github.devapro.droid.export.usecase.SaveDataFileUseCase
import io.github.devapro.droid.export.usecase.SaveJsonFileUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerExportScreenDi() {
    factoryOf(::ExportScreenViewModel)
    factoryOf(::ExportScreenInitStateFactory)
    factoryOf(::FormatsListFactory)
    factoryOf(::ExportScreenFactoryImpl).bind(ExportScreenFactory::class)
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