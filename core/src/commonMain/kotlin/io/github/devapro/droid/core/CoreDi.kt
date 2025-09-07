package io.github.devapro.droid.core

import io.github.devapro.droid.core.mvi.CoroutineContextProvider
import io.github.devapro.droid.core.ui.SnackbarHostStateManager
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf

fun Module.registerCoreDi() {
    factoryOf(::CoroutineContextProvider)
    singleOf(::SnackbarHostStateManager)
}