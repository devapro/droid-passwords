package io.github.devapro.ui.edit

import io.github.devapro.ui.edit.factory.AddEditPasswordScreenInitStateFactory
import io.github.devapro.ui.edit.reducer.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerAddEditPasswordScreenDi() {
    factoryOf(::AddEditPasswordScreenViewModel)
    factoryOf(::AddEditPasswordScreenInitStateFactory)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnTitleChangedReducer)
    factoryOf(::OnUsernameChangedReducer)
    factoryOf(::OnPasswordChangedReducer)
    factoryOf(::OnUrlChangedReducer)
    factoryOf(::OnDescriptionChangedReducer)
    factoryOf(::OnTogglePasswordVisibilityReducer)
    factoryOf(::OnGeneratePasswordReducer)
    factoryOf(::OnAdditionalFieldNameChangedReducer)
    factoryOf(::OnAdditionalFieldValueChangedReducer)
    factoryOf(::OnAddAdditionalFieldReducer)
    factoryOf(::OnRemoveAdditionalFieldReducer)
    factoryOf(::OnSaveClickedReducer)
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnDeleteClickedReducer)
    
    factory {
        AddEditPasswordScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnTitleChangedReducer::class),
                get(OnUsernameChangedReducer::class),
                get(OnPasswordChangedReducer::class),
                get(OnUrlChangedReducer::class),
                get(OnDescriptionChangedReducer::class),
                get(OnTogglePasswordVisibilityReducer::class),
                get(OnGeneratePasswordReducer::class),
                get(OnAdditionalFieldNameChangedReducer::class),
                get(OnAdditionalFieldValueChangedReducer::class),
                get(OnAddAdditionalFieldReducer::class),
                get(OnRemoveAdditionalFieldReducer::class),
                get(OnSaveClickedReducer::class),
                get(OnBackClickedReducer::class),
                get(OnDeleteClickedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 