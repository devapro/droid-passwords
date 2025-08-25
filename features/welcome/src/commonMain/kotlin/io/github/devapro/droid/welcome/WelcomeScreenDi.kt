package io.github.devapro.droid.welcome

import io.github.devapro.droid.welcome.factory.WelcomeScreenFactoryImpl
import io.github.devapro.droid.welcome.factory.WelcomeScreenInitStateFactory
import io.github.devapro.droid.welcome.reducer.InitScreenReducer
import io.github.devapro.droid.welcome.reducer.OnCreateNewVaultReducer
import io.github.devapro.droid.welcome.reducer.OnLoadVaultReducer
import io.github.devapro.droid.welcome.reducer.OnOpenExistingVaultReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerWelcomeScreenDi() {
    factoryOf(::WelcomeScreenViewModel)
    factoryOf(::WelcomeScreenInitStateFactory)
    factoryOf(::WelcomeScreenFactoryImpl).bind(WelcomeScreenFactory::class)
    factoryOf(::WelcomeScreenViewModel)
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