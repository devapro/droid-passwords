package io.github.devapro.droid.settings

import io.github.devapro.droid.data.SettingsRepository
import io.github.devapro.droid.data.SettingsRepositoryImpl
import io.github.devapro.droid.data.ThemeManager
import io.github.devapro.droid.settings.factory.SettingsScreenInitStateFactory
import io.github.devapro.droid.settings.navigation.SettingsScreenFactoryImpl
import io.github.devapro.droid.settings.reducer.InitScreenReducer
import io.github.devapro.droid.settings.reducer.OnBackClickedReducer
import io.github.devapro.droid.settings.reducer.OnChangePasswordClickedReducer
import io.github.devapro.droid.settings.reducer.OnDismissChangePasswordDialogReducer
import io.github.devapro.droid.settings.reducer.OnDismissFilePathDialogReducer
import io.github.devapro.droid.settings.reducer.OnFilePathClickedReducer
import io.github.devapro.droid.settings.reducer.OnFilePathSelectedReducer
import io.github.devapro.droid.settings.reducer.OnLockIntervalChangedReducer
import io.github.devapro.droid.settings.reducer.OnPasswordChangeSubmittedReducer
import io.github.devapro.droid.settings.reducer.OnThemeModeChangedReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerSettingsScreenDi() {
    dataDi()
    factoryOf(::SettingsScreenViewModel)
    factoryOf(::SettingsScreenInitStateFactory)
    factoryOf(::SettingsScreenFactoryImpl).bind(SettingsScreenFactory::class)
    reducersDi()
}

private fun Module.dataDi() {
    // SettingsRepository - interface and implementation
    single<SettingsRepository> { SettingsRepositoryImpl(get(), get()) }

    // ThemeManager - singleton for theme management
    single { ThemeManager(get()) }
}

/**
 * Registers all reducers and the ActionProcessor for the settings screen.
 */
private fun Module.reducersDi() {
    // Reducers with dependencies
    factory { InitScreenReducer(get()) }
    factory { OnLockIntervalChangedReducer(get()) }
    factory { OnThemeModeChangedReducer(get(), get()) }
    factory { OnPasswordChangeSubmittedReducer(get()) }
    factory { OnFilePathSelectedReducer(get()) }

    // Simple reducers without dependencies
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnChangePasswordClickedReducer)
    factoryOf(::OnFilePathClickedReducer)
    factoryOf(::OnDismissChangePasswordDialogReducer)
    factoryOf(::OnDismissFilePathDialogReducer)

    // ActionProcessor with all reducers
    factory {
        SettingsScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnBackClickedReducer::class),
                get(OnChangePasswordClickedReducer::class),
                get(OnFilePathClickedReducer::class),
                get(OnLockIntervalChangedReducer::class),
                get(OnThemeModeChangedReducer::class),
                get(OnPasswordChangeSubmittedReducer::class),
                get(OnFilePathSelectedReducer::class),
                get(OnDismissChangePasswordDialogReducer::class),
                get(OnDismissFilePathDialogReducer::class)
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
}