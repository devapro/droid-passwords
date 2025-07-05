package io.github.devapro.di

import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.data.LocalStorage
import io.github.devapro.data.LockManager
import io.github.devapro.data.vault.CryptoMapper
import io.github.devapro.data.vault.VaultFileRepository
import io.github.devapro.data.vault.VaultRuntimeRepository
import io.github.devapro.features.edit.registerAddEditPasswordScreenDi
import io.github.devapro.features.importexport.registerImportExportScreenDi
import io.github.devapro.features.itemdetails.registerPasswordDetailScreenDi
import io.github.devapro.features.itemslist.registerPasswordListScreenDi
import io.github.devapro.features.setlock.registerSetLockPasswordScreenDi
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
    registerImportExportScreenDi()
    registerSetLockPasswordScreenDi()
    registerUnLockVaultScreenDi()
    registerPasswordListScreenDi()
    registerPasswordDetailScreenDi()
    registerAddEditPasswordScreenDi()
}

private fun Module.coreDi() {
    factoryOf(::CoroutineContextProvider)
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
    single {
        Json { ignoreUnknownKeys = true }
    }
}