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
import io.github.devapro.droid.settings.reducer.OnDismissRemoveDialogReducer
import io.github.devapro.droid.settings.reducer.OnDismissRenameDialogReducer
import io.github.devapro.droid.settings.reducer.OnLockIntervalChangedReducer
import io.github.devapro.droid.settings.reducer.OnPasswordChangeSubmittedReducer
import io.github.devapro.droid.settings.reducer.OnRemoveVaultClickedReducer
import io.github.devapro.droid.settings.reducer.OnRemoveVaultConfirmedReducer
import io.github.devapro.droid.settings.reducer.OnRenameDraftChangedReducer
import io.github.devapro.droid.settings.reducer.OnRenameVaultClickedReducer
import io.github.devapro.droid.settings.reducer.OnRenameVaultSubmittedReducer
import io.github.devapro.droid.settings.reducer.OnThemeModeChangedReducer
import io.github.devapro.droid.settings.reducer.OnToggleAlsoDeleteFileReducer
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
    single<SettingsRepository> {
        SettingsRepositoryImpl(get(), get(), get(), get())
    }
    single { ThemeManager(get()) }
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnLockIntervalChangedReducer)
    factoryOf(::OnThemeModeChangedReducer)
    factoryOf(::OnPasswordChangeSubmittedReducer)
    factoryOf(::OnBackClickedReducer)
    factoryOf(::OnChangePasswordClickedReducer)
    factoryOf(::OnDismissChangePasswordDialogReducer)
    factoryOf(::OnRenameVaultClickedReducer)
    factoryOf(::OnRenameDraftChangedReducer)
    factoryOf(::OnDismissRenameDialogReducer)
    factoryOf(::OnRenameVaultSubmittedReducer)
    factoryOf(::OnRemoveVaultClickedReducer)
    factoryOf(::OnToggleAlsoDeleteFileReducer)
    factoryOf(::OnDismissRemoveDialogReducer)
    factoryOf(::OnRemoveVaultConfirmedReducer)

    factory {
        SettingsScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnBackClickedReducer::class),
                get(OnChangePasswordClickedReducer::class),
                get(OnLockIntervalChangedReducer::class),
                get(OnThemeModeChangedReducer::class),
                get(OnPasswordChangeSubmittedReducer::class),
                get(OnDismissChangePasswordDialogReducer::class),
                get(OnRenameVaultClickedReducer::class),
                get(OnRenameDraftChangedReducer::class),
                get(OnDismissRenameDialogReducer::class),
                get(OnRenameVaultSubmittedReducer::class),
                get(OnRemoveVaultClickedReducer::class),
                get(OnToggleAlsoDeleteFileReducer::class),
                get(OnDismissRemoveDialogReducer::class),
                get(OnRemoveVaultConfirmedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get(),
        )
    }
}
