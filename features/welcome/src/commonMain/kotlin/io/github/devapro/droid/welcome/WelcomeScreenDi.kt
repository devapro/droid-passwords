package io.github.devapro.droid.welcome

import io.github.devapro.droid.welcome.factory.WelcomeScreenFactoryImpl
import io.github.devapro.droid.welcome.factory.WelcomeScreenInitStateFactory
import io.github.devapro.droid.welcome.reducer.InitScreenReducer
import io.github.devapro.droid.welcome.reducer.OnCreateNewVaultReducer
import io.github.devapro.droid.welcome.reducer.OnDismissMasterPasswordDialogReducer
import io.github.devapro.droid.welcome.reducer.OnDismissRestoreAuthDialogReducer
import io.github.devapro.droid.welcome.reducer.OnLoadVaultReducer
import io.github.devapro.droid.welcome.reducer.OnOpenExistingVaultReducer
import io.github.devapro.droid.welcome.reducer.OnRestoreFromSyncClickedReducer
import io.github.devapro.droid.welcome.reducer.OnRestoreLoginSubmittedReducer
import io.github.devapro.droid.welcome.reducer.OnRestoreMasterPasswordSubmittedReducer
import io.github.devapro.droid.welcome.reducer.OnRestoreRegisterSubmittedReducer
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

fun Module.registerWelcomeScreenDi() {
    factoryOf(::WelcomeScreenViewModel)
    factoryOf(::WelcomeScreenInitStateFactory)
    factoryOf(::WelcomeScreenFactoryImpl).bind(WelcomeScreenFactory::class)
    reducersDi()
}

private fun Module.reducersDi() {
    factory { InitScreenReducer(get(), get()) }
    factory { OnRestoreFromSyncClickedReducer(get()) }
    factory { OnRestoreLoginSubmittedReducer(get()) }
    factory { OnRestoreRegisterSubmittedReducer(get()) }
    factory { OnRestoreMasterPasswordSubmittedReducer(get(), get()) }

    factoryOf(::OnCreateNewVaultReducer)
    factoryOf(::OnOpenExistingVaultReducer)
    factoryOf(::OnLoadVaultReducer)
    factoryOf(::OnDismissRestoreAuthDialogReducer)
    factoryOf(::OnDismissMasterPasswordDialogReducer)

    factory {
        WelcomeScreenActionProcessor(
            reducers = setOf(
                get(InitScreenReducer::class),
                get(OnCreateNewVaultReducer::class),
                get(OnOpenExistingVaultReducer::class),
                get(OnLoadVaultReducer::class),
                get(OnRestoreFromSyncClickedReducer::class),
                get(OnDismissRestoreAuthDialogReducer::class),
                get(OnRestoreLoginSubmittedReducer::class),
                get(OnRestoreRegisterSubmittedReducer::class),
                get(OnDismissMasterPasswordDialogReducer::class),
                get(OnRestoreMasterPasswordSubmittedReducer::class),
            ),
            initStateFactory = get(),
            coroutineContextProvider = get()
        )
    }
}
