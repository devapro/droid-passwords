package io.github.devapro.droid.edit

import io.github.devapro.droid.edit.factory.AddEditPasswordScreenFactoryImpl
import io.github.devapro.droid.edit.factory.AddEditPasswordScreenInitStateFactory
import io.github.devapro.droid.edit.reducer.InitScreenReducer
import io.github.devapro.droid.edit.reducer.OnAddAdditionalFieldReducer
import io.github.devapro.droid.edit.reducer.OnAdditionalFieldNameChangedReducer
import io.github.devapro.droid.edit.reducer.OnAdditionalFieldValueChangedReducer
import io.github.devapro.droid.edit.reducer.OnBackClickedReducer
import io.github.devapro.droid.edit.reducer.OnDeleteClickedReducer
import io.github.devapro.droid.edit.reducer.OnDescriptionChangedReducer
import io.github.devapro.droid.edit.reducer.OnGeneratePasswordReducer
import io.github.devapro.droid.edit.reducer.OnPasswordChangedReducer
import io.github.devapro.droid.edit.reducer.OnRemoveAdditionalFieldReducer
import io.github.devapro.droid.edit.reducer.OnSaveClickedReducer
import io.github.devapro.droid.edit.reducer.OnSaveReducer
import io.github.devapro.droid.edit.reducer.OnTagInputChangedReducer
import io.github.devapro.droid.edit.reducer.OnTagRemovedReducer
import io.github.devapro.droid.edit.reducer.OnTagSelectedReducer
import io.github.devapro.droid.edit.reducer.OnTitleChangedReducer
import io.github.devapro.droid.edit.reducer.OnToggleAdditionalFieldsReducer
import io.github.devapro.droid.edit.reducer.OnTogglePasswordVisibilityReducer
import io.github.devapro.droid.edit.reducer.OnUrlChangedReducer
import io.github.devapro.droid.edit.reducer.OnUsernameChangedReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerAddEditPasswordScreenDi() {
    factoryOf(::AddEditPasswordScreenViewModel)
    factoryOf(::AddEditPasswordScreenInitStateFactory)
    factoryOf(::AddEditPasswordScreenFactoryImpl).bind(AddEditPasswordScreenFactory::class)
    reducersDi()
}

private fun Module.reducersDi() {
    factory { InitScreenReducer(get()) }
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
    factoryOf(::OnTagInputChangedReducer)
    factoryOf(::OnTagSelectedReducer)
    factoryOf(::OnTagRemovedReducer)
    factoryOf(::OnSaveClickedReducer)
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnDeleteClickedReducer)
    factoryOf(::OnSaveReducer)

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
                get(OnTagInputChangedReducer::class),
                get(OnTagSelectedReducer::class),
                get(OnTagRemovedReducer::class),
                get(OnSaveClickedReducer::class),
                get(OnBackClickedReducer::class),
                get(OnDeleteClickedReducer::class),
                get(OnSaveReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 