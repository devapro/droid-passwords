package io.github.devapro.di

import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.ui.SnackbarHostStateManager
import io.github.devapro.data.LocalStorage
import io.github.devapro.data.LockManager
import io.github.devapro.data.ThemeManager
import io.github.devapro.data.vault.CryptoMapper
import io.github.devapro.data.vault.VaultFileRepository
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.devapro.features.edit.registerAddEditPasswordScreenDi
import io.github.devapro.features.export.registerExportScreenDi
import io.github.devapro.features.importdata.registerImportScreenDi
import io.github.devapro.features.itemdetails.registerPasswordDetailScreenDi
import io.github.devapro.features.itemslist.registerPasswordListScreenDi
import io.github.devapro.features.setlock.registerSetLockPasswordScreenDi
import io.github.devapro.features.settings.registerSettingsScreenDi
import io.github.devapro.features.tags.registerTagsScreenDi
import io.github.devapro.features.unlock.registerUnLockVaultScreenDi
import io.github.devapro.features.welcome.WelcomeScreenViewModel
import io.github.devapro.features.welcome.registerWelcomeScreenDi
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
    factoriesDi()
    viewModelsDi()
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

private fun Module.factoriesDi() {

}

private fun Module.viewModelsDi() {
    factoryOf(::WelcomeScreenViewModel)
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