package io.github.devapro.droid.importdata

import io.github.devapro.droid.importdata.factory.FormatsListFactory
import io.github.devapro.droid.importdata.factory.ImportScreenFactoryImpl
import io.github.devapro.droid.importdata.factory.ImportScreenInitStateFactory
import io.github.devapro.droid.importdata.reducer.InitScreenReducer
import io.github.devapro.droid.importdata.reducer.OnBackClickedReducer
import io.github.devapro.droid.importdata.reducer.OnConfirmImportPasswordReducer
import io.github.devapro.droid.importdata.reducer.OnConfirmImportReducer
import io.github.devapro.droid.importdata.reducer.OnDismissConfirmDialogReducer
import io.github.devapro.droid.importdata.reducer.OnDismissPasswordDialogReducer
import io.github.devapro.droid.importdata.reducer.OnFormatSelectedReducer
import io.github.devapro.droid.importdata.reducer.OnImportClickedReducer
import io.github.devapro.droid.importdata.reducer.OnImportFileCancelledReducer
import io.github.devapro.droid.importdata.reducer.OnImportFileSelectedReducer
import io.github.devapro.droid.importdata.reducer.OnNewVaultNameChangedReducer
import io.github.devapro.droid.importdata.reducer.OnPasswordChangedReducer
import io.github.devapro.droid.importdata.reducer.OnStrategySelectedReducer
import io.github.devapro.droid.importdata.reducer.OnTargetSelectedReducer
import io.github.devapro.droid.importdata.reducer.OnTogglePasswordVisibilityReducer
import io.github.devapro.droid.importdata.usecase.ApplyImportToActiveVaultUseCase
import io.github.devapro.droid.importdata.usecase.ComputeImportConflictsUseCase
import io.github.devapro.droid.importdata.usecase.CreateVaultFromImportUseCase
import io.github.devapro.droid.importdata.usecase.ParseImportFileUseCase
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
    factoryOf(::ParseImportFileUseCase)
    factoryOf(::ComputeImportConflictsUseCase)
    factoryOf(::ApplyImportToActiveVaultUseCase)
    factoryOf(::CreateVaultFromImportUseCase)
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnFormatSelectedReducer)
    factoryOf(::OnTargetSelectedReducer)
    factoryOf(::OnStrategySelectedReducer)
    factoryOf(::OnNewVaultNameChangedReducer)
    factoryOf(::OnImportClickedReducer)
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnImportFileCancelledReducer)
    factoryOf(::OnImportFileSelectedReducer)
    factoryOf(::OnPasswordChangedReducer)
    factoryOf(::OnTogglePasswordVisibilityReducer)
    factoryOf(::OnConfirmImportPasswordReducer)
    factoryOf(::OnDismissPasswordDialogReducer)
    factoryOf(::OnConfirmImportReducer)
    factoryOf(::OnDismissConfirmDialogReducer)

    factory {
        ImportActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnFormatSelectedReducer::class),
                get(OnTargetSelectedReducer::class),
                get(OnStrategySelectedReducer::class),
                get(OnNewVaultNameChangedReducer::class),
                get(OnImportClickedReducer::class),
                get(OnBackClickedReducer::class),
                get(OnImportFileCancelledReducer::class),
                get(OnImportFileSelectedReducer::class),
                get(OnPasswordChangedReducer::class),
                get(OnTogglePasswordVisibilityReducer::class),
                get(OnConfirmImportPasswordReducer::class),
                get(OnDismissPasswordDialogReducer::class),
                get(OnConfirmImportReducer::class),
                get(OnDismissConfirmDialogReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
}
