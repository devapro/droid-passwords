package io.github.devapro.di

import io.github.devapro.core.mvi.CoroutineContextProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule: Module = module {
    coreDi()
    factoriesDi()
    viewModelsDi()
    reducersDi()
}

private fun Module.coreDi() {
    factoryOf(::CoroutineContextProvider)
}

private fun Module.factoriesDi() {

}

private fun Module.viewModelsDi() {
   // viewModelOf(::MainViewMode)
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