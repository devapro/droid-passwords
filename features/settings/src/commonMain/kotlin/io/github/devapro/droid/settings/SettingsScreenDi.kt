package io.github.devapro.droid.settings

import io.github.devapro.droid.data.SettingsRepository
import io.github.devapro.droid.data.SettingsRepositoryImpl
import io.github.devapro.droid.settings.factory.SettingsScreenInitStateFactory
import io.github.devapro.droid.settings.navigation.SettingsScreenFactoryImpl
import io.github.devapro.droid.settings.reducer.InitScreenReducer
import io.github.devapro.droid.settings.reducer.OnBackClickedReducer
import io.github.devapro.droid.settings.reducer.OnChangePasswordClickedReducer
import io.github.devapro.droid.settings.reducer.OnDismissAuthDialogReducer
import io.github.devapro.droid.settings.reducer.OnDismissChangePasswordDialogReducer
import io.github.devapro.droid.settings.reducer.OnDismissRemoveDialogReducer
import io.github.devapro.droid.settings.reducer.OnDismissRenameDialogReducer
import io.github.devapro.droid.settings.reducer.OnDismissServerUrlDialogReducer
import io.github.devapro.droid.settings.reducer.OnLockIntervalChangedReducer
import io.github.devapro.droid.settings.reducer.OnDismissLinkMasterPasswordDialogReducer
import io.github.devapro.droid.settings.reducer.OnLinkMasterPasswordExecuteReducer
import io.github.devapro.droid.settings.reducer.OnLinkMasterPasswordSubmittedReducer
import io.github.devapro.droid.settings.reducer.OnLoginClickedReducer
import io.github.devapro.droid.settings.reducer.OnLoginSubmittedReducer
import io.github.devapro.droid.settings.reducer.OnLogoutClickedReducer
import io.github.devapro.droid.settings.reducer.OnPasswordChangeSubmittedReducer
import io.github.devapro.droid.settings.reducer.OnRemoveVaultClickedReducer
import io.github.devapro.droid.settings.reducer.OnRemoveVaultConfirmedReducer
import io.github.devapro.droid.settings.reducer.OnRenameDraftChangedReducer
import io.github.devapro.droid.settings.reducer.OnRenameVaultClickedReducer
import io.github.devapro.droid.settings.reducer.OnRenameVaultSubmittedReducer
import io.github.devapro.droid.settings.reducer.OnPeriodicIntervalChangedReducer
import io.github.devapro.droid.settings.reducer.OnPeriodicSyncToggledReducer
import io.github.devapro.droid.settings.reducer.OnRegisterSubmittedReducer
import io.github.devapro.droid.settings.reducer.OnServerUrlClickedReducer
import io.github.devapro.droid.settings.reducer.OnServerUrlSubmittedReducer
import io.github.devapro.droid.settings.reducer.OnServerUrlValidateReducer
import io.github.devapro.droid.settings.reducer.OnSyncFromServerClickedReducer
import io.github.devapro.droid.settings.reducer.OnSyncFromServerExecuteReducer
import io.github.devapro.droid.settings.reducer.OnSyncNowClickedReducer
import io.github.devapro.droid.settings.reducer.OnSyncNowExecuteReducer
import io.github.devapro.droid.settings.reducer.OnSyncToServerClickedReducer
import io.github.devapro.droid.settings.reducer.OnSyncToServerExecuteReducer
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
}

private fun Module.reducersDi() {
    // Reducers with dependencies (resolved by type via factoryOf)
    factoryOf(::InitScreenReducer)
    factoryOf(::OnLockIntervalChangedReducer)
    factoryOf(::OnThemeModeChangedReducer)
    factoryOf(::OnPasswordChangeSubmittedReducer)

    // Sync reducers with dependencies
    factoryOf(::OnServerUrlValidateReducer)
    factoryOf(::OnRegisterSubmittedReducer)
    factoryOf(::OnLoginSubmittedReducer)
    factoryOf(::OnLinkMasterPasswordExecuteReducer)
    factoryOf(::OnLogoutClickedReducer)
    factoryOf(::OnSyncNowExecuteReducer)
    factoryOf(::OnSyncToServerExecuteReducer)
    factoryOf(::OnSyncFromServerExecuteReducer)
    factoryOf(::OnPeriodicSyncToggledReducer)
    factoryOf(::OnPeriodicIntervalChangedReducer)

    // Simple reducers without dependencies
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
    factoryOf(::OnServerUrlClickedReducer)
    factoryOf(::OnServerUrlSubmittedReducer)
    factoryOf(::OnDismissServerUrlDialogReducer)
    factoryOf(::OnLoginClickedReducer)
    factoryOf(::OnDismissAuthDialogReducer)
    factoryOf(::OnLinkMasterPasswordSubmittedReducer)
    factoryOf(::OnDismissLinkMasterPasswordDialogReducer)
    factoryOf(::OnSyncNowClickedReducer)
    factoryOf(::OnSyncToServerClickedReducer)
    factoryOf(::OnSyncFromServerClickedReducer)

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
                get(OnServerUrlClickedReducer::class),
                get(OnServerUrlSubmittedReducer::class),
                get(OnServerUrlValidateReducer::class),
                get(OnDismissServerUrlDialogReducer::class),
                get(OnLoginClickedReducer::class),
                get(OnDismissAuthDialogReducer::class),
                get(OnRegisterSubmittedReducer::class),
                get(OnLoginSubmittedReducer::class),
                get(OnLinkMasterPasswordSubmittedReducer::class),
                get(OnLinkMasterPasswordExecuteReducer::class),
                get(OnDismissLinkMasterPasswordDialogReducer::class),
                get(OnLogoutClickedReducer::class),
                get(OnSyncNowClickedReducer::class),
                get(OnSyncNowExecuteReducer::class),
                get(OnSyncToServerClickedReducer::class),
                get(OnSyncToServerExecuteReducer::class),
                get(OnSyncFromServerClickedReducer::class),
                get(OnSyncFromServerExecuteReducer::class),
                get(OnPeriodicSyncToggledReducer::class),
                get(OnPeriodicIntervalChangedReducer::class)
            ),
            initStateFactory = get(),
            coroutineContextProvider = get(),
        )
    }
}
