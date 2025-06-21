package io.github.devapro.ui.welcome

import io.github.devapro.ui.welcome.factory.WelcomeScreenInitStateFactory
import io.github.devapro.ui.welcome.reducer.InitScreenReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerWelcomeScreenDi() {
    factoryOf(::WelcomeScreenViewModel)
    factoryOf(::WelcomeScreenInitStateFactory)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factory {
        WelcomeScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
}