package io.github.devapro.di

import io.github.devapro.droid.core.registerCoreDi
import io.github.devapro.droid.data.registerDataDi
import io.github.devapro.droid.edit.registerAddEditPasswordScreenDi
import io.github.devapro.droid.export.registerExportScreenDi
import io.github.devapro.droid.importdata.registerImportScreenDi
import io.github.devapro.droid.itemdetails.registerPasswordDetailScreenDi
import io.github.devapro.droid.itemslist.registerPasswordListScreenDi
import io.github.devapro.droid.setlock.registerSetLockPasswordScreenDi
import io.github.devapro.droid.settings.registerSettingsScreenDi
import io.github.devapro.droid.tags.registerTagsScreenDi
import io.github.devapro.droid.unlock.registerUnLockVaultScreenDi
import io.github.devapro.droid.welcome.registerWelcomeScreenDi
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(){
    startKoin {
        modules(appModule)
    }
}

val appModule: Module = module {
    registerCoreDi()
    registerDataDi()

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