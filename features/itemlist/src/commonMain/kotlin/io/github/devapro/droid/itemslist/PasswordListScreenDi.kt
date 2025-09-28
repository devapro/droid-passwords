package io.github.devapro.droid.itemslist

import io.github.devapro.droid.itemlist.PasswordListScreenFactory
import io.github.devapro.droid.itemslist.factory.PasswordListScreenInitStateFactory
import io.github.devapro.droid.itemslist.mapper.VaultItemMapper
import io.github.devapro.droid.itemslist.navigation.PasswordListScreenFactoryImpl
import io.github.devapro.droid.itemslist.reducer.InitScreenReducer
import io.github.devapro.droid.itemslist.reducer.OnAddPasswordClickedReducer
import io.github.devapro.droid.itemslist.reducer.OnBackClickedReducer
import io.github.devapro.droid.itemslist.reducer.OnClearSearchReducer
import io.github.devapro.droid.itemslist.reducer.OnDeletePasswordClickedReducer
import io.github.devapro.droid.itemslist.reducer.OnExportClickedReducer
import io.github.devapro.droid.itemslist.reducer.OnPasswordItemClickedReducer
import io.github.devapro.droid.itemslist.reducer.OnSearchChangedReducer
import io.github.devapro.droid.itemslist.reducer.OnSettingsClickedReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerPasswordListScreenDi() {
    factoryOf(::PasswordListScreenViewModel)
    factoryOf(::PasswordListScreenInitStateFactory)
    factoryOf(::VaultItemMapper)
    factoryOf(::PasswordListScreenFactoryImpl).bind(PasswordListScreenFactory::class)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnSearchChangedReducer)
    factoryOf(::OnAddPasswordClickedReducer)
    factoryOf(::OnPasswordItemClickedReducer)
    factoryOf(::OnDeletePasswordClickedReducer)
    factoryOf(::OnExportClickedReducer)
    factoryOf(::OnSettingsClickedReducer)
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnClearSearchReducer)
    
    factory {
        PasswordListScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnSearchChangedReducer::class),
                get(OnAddPasswordClickedReducer::class),
                get(OnPasswordItemClickedReducer::class),
                get(OnDeletePasswordClickedReducer::class),
                get(OnExportClickedReducer::class),
                get(OnSettingsClickedReducer::class),
                get(OnBackClickedReducer::class),
                get(OnClearSearchReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
} 