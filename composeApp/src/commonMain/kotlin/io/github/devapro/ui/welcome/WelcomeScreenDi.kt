package io.github.devapro.ui.welcome

import io.github.devapro.ui.welcome.factory.WelcomeScreenInitStateFactory
import io.github.devapro.ui.welcome.reducer.InitScreenReducer
import io.github.devapro.ui.welcome.reducer.OnCreateNewVaultReducer
import io.github.devapro.ui.welcome.reducer.OnOpenExistingVaultReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

fun Module.registerWelcomeScreenDi() {
    factoryOf(::WelcomeScreenViewModel)
    factoryOf(::WelcomeScreenInitStateFactory)
    reducersDi()
}

private fun Module.reducersDi() {
    factoryOf(::InitScreenReducer)
    factoryOf(::OnCreateNewVaultReducer)
    factoryOf(::OnOpenExistingVaultReducer)
    factory {
        WelcomeScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnCreateNewVaultReducer::class),
                get(OnOpenExistingVaultReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
}