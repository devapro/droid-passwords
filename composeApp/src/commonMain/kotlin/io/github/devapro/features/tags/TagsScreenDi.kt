package io.github.devapro.features.tags

import io.github.devapro.features.tags.factory.TagsScreenInitStateFactory
import io.github.devapro.features.tags.mapper.TagsMapper
import io.github.devapro.features.tags.reducer.InitScreenReducer
import io.github.devapro.features.tags.reducer.OnAddPasswordClickedReducer
import io.github.devapro.features.tags.reducer.OnBackClickedReducer
import io.github.devapro.features.tags.reducer.OnClearSearchReducer
import io.github.devapro.features.tags.reducer.OnImportExportClickedReducer
import io.github.devapro.features.tags.reducer.OnRefreshReducer
import io.github.devapro.features.tags.reducer.OnSearchChangedReducer
import io.github.devapro.features.tags.reducer.OnSettingsClickedReducer
import io.github.devapro.features.tags.reducer.OnTagClickedReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerTagsScreenDi() {
    factoryOf(::TagsScreenViewModel)
    factoryOf(::TagsScreenInitStateFactory)
    factoryOf(::TagsMapper)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnSearchChangedReducer)
    factoryOf(::OnTagClickedReducer)
    factoryOf(::OnRefreshReducer)
    factoryOf(::OnClearSearchReducer)
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnAddPasswordClickedReducer)
    factoryOf(::OnSettingsClickedReducer)
    factoryOf(::OnImportExportClickedReducer)

    factory {
        TagsScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnSearchChangedReducer::class),
                get(OnTagClickedReducer::class),
                get(OnRefreshReducer::class),
                get(OnClearSearchReducer::class),
                get(OnBackClickedReducer::class),
                get(OnAddPasswordClickedReducer::class),
                get(OnSettingsClickedReducer::class),
                get(OnImportExportClickedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 