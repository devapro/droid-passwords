package io.github.devapro.features.welcome

import io.github.devapro.features.welcome.factory.WelcomeScreenInitStateFactory
import io.github.devapro.features.welcome.reducer.InitScreenReducer
import io.github.devapro.features.welcome.reducer.OnCreateNewVaultReducer
import io.github.devapro.features.welcome.reducer.OnLoadVaultReducer
import io.github.devapro.features.welcome.reducer.OnOpenExistingVaultReducer
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
    factoryOf(::OnLoadVaultReducer)
    factory {
        WelcomeScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnCreateNewVaultReducer::class),
                get(OnOpenExistingVaultReducer::class),
                get(OnLoadVaultReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
}