package io.github.devapro.features.edit

import io.github.devapro.features.edit.factory.AddEditPasswordScreenInitStateFactory
import io.github.devapro.features.edit.reducer.InitScreenReducer
import io.github.devapro.features.edit.reducer.OnAddAdditionalFieldReducer
import io.github.devapro.features.edit.reducer.OnAdditionalFieldNameChangedReducer
import io.github.devapro.features.edit.reducer.OnAdditionalFieldValueChangedReducer
import io.github.devapro.features.edit.reducer.OnBackClickedReducer
import io.github.devapro.features.edit.reducer.OnDeleteClickedReducer
import io.github.devapro.features.edit.reducer.OnDescriptionChangedReducer
import io.github.devapro.features.edit.reducer.OnGeneratePasswordReducer
import io.github.devapro.features.edit.reducer.OnPasswordChangedReducer
import io.github.devapro.features.edit.reducer.OnRemoveAdditionalFieldReducer
import io.github.devapro.features.edit.reducer.OnSaveClickedReducer
import io.github.devapro.features.edit.reducer.OnTitleChangedReducer
import io.github.devapro.features.edit.reducer.OnToggleAdditionalFieldsReducer
import io.github.devapro.features.edit.reducer.OnTogglePasswordVisibilityReducer
import io.github.devapro.features.edit.reducer.OnUrlChangedReducer
import io.github.devapro.features.edit.reducer.OnUsernameChangedReducer
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
    factoryOf(::OnToggleAdditionalFieldsReducer)
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
                get(OnToggleAdditionalFieldsReducer::class),
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