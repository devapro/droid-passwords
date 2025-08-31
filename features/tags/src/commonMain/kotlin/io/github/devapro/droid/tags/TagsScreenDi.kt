package io.github.devapro.droid.tags

import io.github.devapro.droid.tags.factory.TagsScreenInitStateFactory
import io.github.devapro.droid.tags.mapper.TagsMapper
import io.github.devapro.droid.tags.navigation.TagsScreenFactoryImpl
import io.github.devapro.droid.tags.reducer.InitScreenReducer
import io.github.devapro.droid.tags.reducer.OnAddPasswordClickedReducer
import io.github.devapro.droid.tags.reducer.OnBackClickedReducer
import io.github.devapro.droid.tags.reducer.OnClearSearchReducer
import io.github.devapro.droid.tags.reducer.OnImportExportClickedReducer
import io.github.devapro.droid.tags.reducer.OnRefreshReducer
import io.github.devapro.droid.tags.reducer.OnSearchChangedReducer
import io.github.devapro.droid.tags.reducer.OnSettingsClickedReducer
import io.github.devapro.droid.tags.reducer.OnTagClickedReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerTagsScreenDi() {
    factoryOf(::TagsScreenViewModel)
    factoryOf(::TagsScreenInitStateFactory)
    factoryOf(::TagsMapper)
    factoryOf(::TagsScreenFactoryImpl).bind(TagsScreenFactory::class)
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