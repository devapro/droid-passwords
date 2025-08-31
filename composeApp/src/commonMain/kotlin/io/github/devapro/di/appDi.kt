package io.github.devapro.di

import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.ui.SnackbarHostStateManager
import io.github.devapro.droid.data.LocalStorage
import io.github.devapro.droid.data.LockManager
import io.github.devapro.droid.data.ThemeManager
import io.github.devapro.droid.data.vault.CryptoMapper
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import io.github.devapro.droid.edit.registerAddEditPasswordScreenDi
import io.github.devapro.droid.export.registerExportScreenDi
import io.github.devapro.droid.importdata.registerImportScreenDi
import io.github.devapro.droid.itemdetails.registerPasswordDetailScreenDi
import io.github.devapro.droid.itemslist.registerPasswordListScreenDi
import io.github.devapro.droid.setlock.registerSetLockPasswordScreenDi
import io.github.devapro.droid.welcome.registerWelcomeScreenDi
import io.github.devapro.features.settings.registerSettingsScreenDi
import io.github.devapro.features.tags.registerTagsScreenDi
import io.github.devapro.features.unlock.registerUnLockVaultScreenDi
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun initKoin(){
    startKoin {
        modules(appModule)
    }
}

val appModule: Module = module {
    coreDi()
    dataDi()

    registerWelcomeScreenDi()
    registerImportScreenDi()
    registerExportScreenDi()
    registerSetLockPasswordScreenDi()
    registerUnLockVaultScreenDi()
    registerPasswordListScreenDi()
    registerPasswordDetailScreenDi()
    registerAddEditPasswordScreenDi()
    registerSettingsScreenDi()
    registerTagsScreenDi()
}

private fun Module.coreDi() {
    factoryOf(::CoroutineContextProvider)
    singleOf(::SnackbarHostStateManager)
}

private fun Module.dataDi() {
     factoryOf(::LocalStorage)
     single { LockManager }
    factoryOf(::VaultFileRepository)
    singleOf(::VaultRuntimeRepository)
    singleOf(::CryptoMapper)
    singleOf(::ThemeManager)
    single {
        Json { ignoreUnknownKeys = true }
    }
}