package io.github.devapro.di

import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.data.LocalStorage
import io.github.devapro.data.LockManager
import io.github.devapro.ui.importexport.registerImportExportScreenDi
import io.github.devapro.ui.itemslist.registerPasswordListScreenDi
import io.github.devapro.ui.setlock.registerSetLockPasswordScreenDi
import io.github.devapro.ui.unlock.registerUnLockVaultScreenDi
import io.github.devapro.ui.welcome.WelcomeScreenViewModel
import io.github.devapro.ui.welcome.registerWelcomeScreenDi
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
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
    reducersDi()
    dataDi()

    registerWelcomeScreenDi()
    registerImportExportScreenDi()
    registerSetLockPasswordScreenDi()
    registerUnLockVaultScreenDi()
    registerPasswordListScreenDi()
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
}

private fun Module.reducersDi() {
//    factoryOf(::ChangeScreenReducer)
//    factoryOf(::InitAppReducer)
//    factoryOf(::CheckPermissionsReducer)
//    factory {
//        MainActionProcessor(
//            reducers = setOf(
//                get(ChangeScreenReducer::class),
//                get(InitAppReducer::class),
//                get(CheckPermissionsReducer::class)
//            ),
//            initStateFactory = get(),
//            coroutineContextProvider = get()
//        )
//    }
}